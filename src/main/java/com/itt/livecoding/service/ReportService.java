package com.itt.livecoding.service;

import com.itt.livecoding.constant.OrderStatus;
import com.itt.livecoding.dto.response.CustomerResponse;
import com.itt.livecoding.dto.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReportService {

    private final OrderService orderService;
    private final CustomerService customerService;

    public ReportService(
            OrderService orderService,
            CustomerService customerService
    ) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    public String generateOrderReport(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        StringBuilder report = new StringBuilder("Order Report\n");
        int totalOrders = 0;
        double totalRevenue = 0;

        List<OrderResponse> orders = orderService.findAllOrders();

        for (OrderResponse order : orders) {
            if (isDateInRange(order, startDate, endDate)) {
                if (!order.orderStatus.equals(OrderStatus.CANCELLED)) {
                    totalOrders++;
                    totalRevenue += order.finalAmount;

                    CustomerResponse customer = customerService.findCustomerById(order.customerId);
                    if (Objects.nonNull(customer)) {
                        report
                                .append("\nOrder ID: ").append(order.id)
                                .append("\nCustomer: ").append(customer.name)
                                .append("\nAmount: $").append(String.format("%.2f", order.finalAmount))
                                .append("\nStatus: ").append(order.orderStatus)
                                .append("\n-------------------");
                    }
                }
            }
        }

        report
                .append("\n\nTotal Orders: ").append(totalOrders)
                .append("\nTotal Revenue: $").append(String.format("%.2f", totalRevenue));
        return report.toString();
    }

    private boolean isDateInRange(OrderResponse order, LocalDateTime startDate, LocalDateTime endDate) {
        return order.createdAt.isAfter(startDate) && order.createdAt.isBefore(endDate);
    }

}
