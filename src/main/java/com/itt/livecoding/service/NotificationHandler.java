package com.itt.livecoding.service;

import com.itt.livecoding.constant.Role;
import com.itt.livecoding.dto.NotificationMetadata;
import com.itt.livecoding.dto.response.CustomerResponse;
import com.itt.livecoding.entities.Order;
import com.itt.livecoding.factory.NotificationFactory;

public class NotificationHandler {

    private final CustomerService customerService;

    public NotificationHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void notifyOrderProcessing(Order order) {
        CustomerResponse customer = customerService.findCustomerById(order.customerId);
        NotificationMetadata metadata = new NotificationMetadata(
                Role.CUSTOMER,
                customer.email,
                "Order " + order.id + " is being processed"
        );
        NotificationService service = NotificationFactory.getInstance(metadata);
        service.sendNotification();
    }
}
