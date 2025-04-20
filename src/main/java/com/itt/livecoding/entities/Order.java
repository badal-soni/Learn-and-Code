package com.itt.livecoding.entities;


import com.itt.livecoding.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    public String id;
    public String customerId;
    public List<OrderItem> items = new ArrayList<>();
    public OrderStatus status = OrderStatus.PENDING;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public double totalAmount;
    public double discountApplied;
    public double finalAmount;

}
