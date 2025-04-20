package com.itt.livecoding.mapper;

import com.itt.livecoding.dto.response.CustomerResponse;
import com.itt.livecoding.dto.response.OrderResponse;
import com.itt.livecoding.entities.Customer;
import com.itt.livecoding.entities.Order;
import org.jetbrains.annotations.NotNull;

public class Mapper {

    public static OrderResponse mapToOrderResponse(@NotNull Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.id = order.id;
        orderResponse.orderStatus = order.status;
        orderResponse.createdAt = order.createdAt;
        orderResponse.totalAmount = order.totalAmount;
        orderResponse.discountApplied = order.discountApplied;
        orderResponse.finalAmount = order.finalAmount;
        orderResponse.items = order.items;
        orderResponse.customerId = order.customerId;
        return orderResponse;
    }

    public static CustomerResponse mapToCustomerResponse(@NotNull Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.id = customer.id;;
        customerResponse.name = customer.name;
        customerResponse.email = customer.email;
        customerResponse.phone = customer.phone;
        customerResponse.membershipLevel = customer.membershipLevel;
        return customerResponse;
    }

}
