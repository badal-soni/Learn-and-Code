package com.itt.livecoding.entities;

import com.itt.livecoding.constant.MemberShipLevel;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    public String id;
    public String name;
    public String email;
    public String address;
    public String phone;
    public MemberShipLevel membershipLevel;
    public List<Order> orderHistory = new ArrayList<>();

}
