package com.itt.livecoding.repository;

import com.itt.livecoding.entities.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepository {

    private static final Map<String, Customer> customerDatabase = new HashMap<>();

    public void save(Customer customer) {
        customerDatabase.put(customer.id, customer);
    }

    public Customer findCustomerById(String id) {
        return customerDatabase.get(id);
    }

}
