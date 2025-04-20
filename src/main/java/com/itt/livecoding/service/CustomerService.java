package com.itt.livecoding.service;

import com.itt.livecoding.constant.MemberShipLevel;
import com.itt.livecoding.dto.response.CustomerResponse;
import com.itt.livecoding.entities.Customer;
import com.itt.livecoding.exception.NotFoundException;
import com.itt.livecoding.mapper.Mapper;
import com.itt.livecoding.repository.CustomerRepository;

import java.util.Objects;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse findCustomerById(String customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (Objects.isNull(customer)) {
            throw new NotFoundException("Customer with id: " + customerId + " not found");
        }
        return Mapper.mapToCustomerResponse(customer);
    }

    public MemberShipLevel findCustomerMemberShipLevel(String customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        return customer.membershipLevel;
    }

}
