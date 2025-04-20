package com.itt.livecoding.repository;

import com.itt.livecoding.entities.Order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderRepository {

    private static final String ORDER = "Order_";
    private static int orderCounter = 1;

    private static final Map<String, Order> orderDatabase = new HashMap<>();

    public Order saveOrder(Order order) {
        order.id = ORDER + (orderCounter++);
        if (orderDatabase.get(order.id) == null) {
            order.createdAt = LocalDateTime.now();
        }
        order.updatedAt = LocalDateTime.now();
        orderDatabase.put(order.id, order);
        return order;
    }

    public Optional<Order> findOrderById(String orderId) {
        return Optional.ofNullable(orderDatabase.get(orderId));
    }

    public List<Order> findAllOrders() {
        return orderDatabase
                .values()
                .stream()
                .toList();
    }

}
