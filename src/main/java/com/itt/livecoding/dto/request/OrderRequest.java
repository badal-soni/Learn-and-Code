package com.itt.livecoding.dto.request;

import com.itt.livecoding.entities.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    public String customerId;
    public List<OrderItem> items = new ArrayList<>();

}
