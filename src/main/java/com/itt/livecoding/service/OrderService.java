package com.itt.livecoding.service;

import com.itt.livecoding.constant.ErrorMessage;
import com.itt.livecoding.constant.MemberShipLevel;
import com.itt.livecoding.constant.OrderStatus;
import com.itt.livecoding.constant.Role;
import com.itt.livecoding.dto.NotificationMetadata;
import com.itt.livecoding.dto.request.OrderRequest;
import com.itt.livecoding.dto.response.CustomerResponse;
import com.itt.livecoding.dto.response.OrderResponse;
import com.itt.livecoding.entities.Order;
import com.itt.livecoding.entities.OrderItem;
import com.itt.livecoding.exception.NotFoundException;
import com.itt.livecoding.factory.NotificationFactory;
import com.itt.livecoding.mapper.Mapper;
import com.itt.livecoding.repository.OrderRepository;
import com.itt.livecoding.util.Utility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderRepository orderRepository;
    private final StockService stockService;
    private final CustomerService customerService;

    private static final int POSITIVE_MULTIPLIER = 1;
    private static final int NEGATIVE_MULTIPLIER = -1;

    public OrderService(
            OrderRepository orderRepository,
            StockService stockService,
            CustomerService customerService
    ) {
        this.orderRepository = orderRepository;
        this.stockService = stockService;
        this.customerService = customerService;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        validateStock(orderRequest.items);

        double discountedPrice = calculatePrice(orderRequest);
        MemberShipLevel memberShipLevel = customerService.findCustomerMemberShipLevel(orderRequest.customerId);

        Order order = new Order();
        order.customerId = orderRequest.customerId;
        order.discountApplied = Utility.getDiscountPercentageFromCustomerMembershipLevel(memberShipLevel);
        order.totalAmount = getTotalAmount(orderRequest);
        order.finalAmount = discountedPrice;

        order = orderRepository.saveOrder(order);
        updateStock(order, NEGATIVE_MULTIPLIER);
        sendNotification(order);

        return Mapper.mapToOrderResponse(order);
    }

    private void validateStock(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            final String productId = orderItem.productId;
            if (!stockService.isInStock(productId)) {
                throw new NotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND, productId));
            }
        }
    }

    private double calculatePrice(OrderRequest orderRequest) {
        final CustomerResponse customerResponseDTO = customerService.findCustomerById(orderRequest.customerId);
        final MemberShipLevel memberShipLevel = customerResponseDTO.membershipLevel;
        double price = getTotalAmount(orderRequest);

        final double discountPercent = Utility.getDiscountPercentageFromCustomerMembershipLevel(memberShipLevel);

        return price * (100 - discountPercent) / 100;
    }

    private double getTotalAmount(OrderRequest orderRequest) {
        double price = 0;
        for (OrderItem orderItem : orderRequest.items) {
            price += orderItem.price;
        }
        return price;
    }

    private void updateStock(Order orderRequest, int multiplier) {
        for (OrderItem orderItem : orderRequest.items) {
            stockService.updateStock(orderItem.productId, multiplier * orderItem.quantity);
        }
    }

    private void sendNotification(Order order) {
        CustomerResponse customer = customerService.findCustomerById(order.customerId);
        NotificationMetadata notificationMetadata = new NotificationMetadata(
                Role.CUSTOMER,
                customer.email,
                "Order " + order.id + " is being processed"
        );
        NotificationService notificationService = NotificationFactory.getInstance(notificationMetadata);
        notificationService.sendNotification();
    }

    public OrderResponse cancelOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findOrderById(orderId);

        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Order with id: " + orderId + " not found");
        }

        Order order = orderOptional.get();
        order.status = OrderStatus.CANCELLED;
        orderRepository.saveOrder(order);

        updateStock(order, POSITIVE_MULTIPLIER);
        sendNotification(order);

        return Mapper.mapToOrderResponse(order);
    }

    public List<OrderResponse> findAllOrders() {
        return orderRepository
                .findAllOrders()
                .stream()
                .map(Mapper::mapToOrderResponse)
                .collect(Collectors.toList());
    }

}
