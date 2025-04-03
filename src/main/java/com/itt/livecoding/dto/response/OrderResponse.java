package com.itt.livecoding.dto.response;

import com.itt.livecoding.constant.OrderStatus;
import com.itt.livecoding.entities.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    public String id;
    public List<OrderItem> items;
    public OrderStatus orderStatus;
    public LocalDateTime createdAt;
    public double totalAmount;
    public double discountApplied;
    public double finalAmount;
    public String customerId;

}
