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
    private final NotificationHandler notificationHandler;
    private final PricingService pricingService;

    private static final int POSITIVE_MULTIPLIER = 1;
    private static final int NEGATIVE_MULTIPLIER = -1;

    public OrderService(
            OrderRepository orderRepository,
            StockService stockService,
            CustomerService customerService,
            NotificationHandler notificationHandler,
            PricingService pricingService
    ) {
        this.orderRepository = orderRepository;
        this.stockService = stockService;
        this.customerService = customerService;
        this.notificationHandler = notificationHandler;
        this.pricingService = pricingService;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        validateStock(orderRequest.items);

        double discountedPrice = pricingService.calculateFinalPrice(orderRequest);
        MemberShipLevel memberShipLevel = customerService.findCustomerMemberShipLevel(orderRequest.customerId);

        Order order = new Order();
        order.customerId = orderRequest.customerId;
        order.discountApplied = Utility.getDiscountRate(memberShipLevel);
        order.totalAmount = pricingService.getTotalAmount(orderRequest);
        order.finalAmount = discountedPrice;

        order = orderRepository.saveOrder(order);
        stockService.updateStockForOrder(order, NEGATIVE_MULTIPLIER);
        notificationHandler.notifyOrderProcessing(order);

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



    public OrderResponse cancelOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findOrderById(orderId);

        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Order with id: " + orderId + " not found");
        }

        Order order = orderOptional.get();
        order.status = OrderStatus.CANCELLED;
        orderRepository.saveOrder(order);

        stockService.updateStockForOrder(order, POSITIVE_MULTIPLIER);
        notificationHandler.notifyOrderProcessing(order);

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
