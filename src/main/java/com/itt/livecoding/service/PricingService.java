package com.itt.livecoding.service;

import com.itt.livecoding.constant.MemberShipLevel;
import com.itt.livecoding.dto.request.OrderRequest;
import com.itt.livecoding.util.Utility;

public class PricingService {

    private final CustomerService customerService;

    public PricingService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public double calculateFinalPrice(OrderRequest orderRequest) {
        double total = getTotalAmount(orderRequest);
        MemberShipLevel level = customerService.findCustomerMemberShipLevel(orderRequest.customerId);
        double discount = Utility.getDiscountRate(level);
        return total * (1 - discount);
    }

    public double getTotalAmount(OrderRequest orderRequest) {
        return orderRequest.items.stream().mapToDouble(item -> item.price).sum();
    }
}
