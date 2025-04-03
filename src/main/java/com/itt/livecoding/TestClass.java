package com.itt.livecoding;

import java.util.*;
import java.time.LocalDateTime;

class OrderProcessor {
    private static List<Order> activeOrders = new ArrayList<>();
    private static List<String> processingQueue = new ArrayList<>();

    public Map<String, Customer> customers = new HashMap<>();
    public Map<String, Product> products = new HashMap<>();
    public Map<String, Order> orderDatabase = new HashMap<>();
    public Map<String, Order> customerDatabase = new HashMap<>();

    public boolean processOrder(String orderId) {
        System.out.println("Processing order: " + orderId);

        Order order = orderDatabase.get(orderId);
        if (order == null) {
            System.out.println("Order not found");
            return false;
        }

        Customer customer = customers.get(order.customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return false;
        }

        for (OrderItem item : order.items) {
            Product product = products.get(item.productId);
            if (product == null) {
                System.out.println("Product " + item.productId + " not found");
                return false;
            }
            if (product.stock < item.quantity) {
                System.out.println("Insufficient stock for product " + item.productId);
                return false;
            }
        }

        for (OrderItem item : order.items) {
            Product product = products.get(item.productId);
            if (product != null) {
                product.stock -= item.quantity;
                products.put(item.productId, product);
            }
        }

        double discount = 0;
        if (customer.membershipLevel.equals("gold")) {
            discount = 0.1;
        } else if (customer.membershipLevel.equals("platinum")) {
            discount = 0.15;
        } else if (customer.membershipLevel.equals("diamond")) {
            discount = 0.2;
        }

        if (order.totalAmount > 1000) {
            discount += 0.05;
        }

        order.status = "processing";
        order.discountApplied = order.totalAmount * discount;
        order.finalAmount = order.totalAmount - order.discountApplied;
        order.updatedAt = LocalDateTime.now();

        orderDatabase.put(orderId, order);
        activeOrders.add(order);
        processingQueue.add(orderId);

        sendCustomerNotification(customer.email, "Order " + orderId + " is being processed");
        sendAdminNotification("New order processing: " + orderId);

        return true;
    }

    public boolean cancelOrder(String orderId) {
        System.out.println("Cancelling order: " + orderId);

        Order order = orderDatabase.get(orderId);
        if (order == null) {
            System.out.println("Order not found");
            return false;
        }

        Customer customer = customers.get(order.customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return false;
        }

        for (OrderItem item : order.items) {
            Product product = products.get(item.productId);
            if (product != null) {
                product.stock += item.quantity;
                products.put(item.productId, product);
            }
        }

        order.status = "cancelled";
        order.updatedAt = LocalDateTime.now();

        orderDatabase.put(orderId, order);
        activeOrders.removeIf(o -> o.id.equals(orderId));
        processingQueue.removeIf(id -> id.equals(orderId));

        sendCustomerNotification(customer.email, "Order " + orderId + " has been cancelled");
        sendAdminNotification("Order cancelled: " + orderId);

        return true;
    }

    private void sendCustomerNotification(String email, String message) {
        try {
            System.out.println("Sending email to " + email + ": " + message);
            Thread.sleep(100);
        } catch (Exception e) {
            System.out.println("Failed to send customer notification");
        }
    }

    private void sendAdminNotification(String message) {
        try {
            System.out.println("Admin notification: " + message);
            Thread.sleep(50);
        } catch (Exception e) {
            System.out.println("Failed to send admin notification");
        }
    }

    public boolean addCustomer(Customer customer) {
        if (customer.id == null || customer.id.isEmpty() || customer.email == null || customer.email.isEmpty()) {
            return false;
        }
        customers.put(customer.id, customer);
        return true;
    }

    public boolean addProduct(Product product) {
        if (product.id == null || product.id.isEmpty() || product.price < 0) {
            return false;
        }
        products.put(product.id, product);
        return true;
    }

    public String generateOrderReport(LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder report = new StringBuilder("Order Report\n");
        int totalOrders = 0;
        double totalRevenue = 0;

        for (Order order : orderDatabase.values()) {
            if (order.createdAt.isAfter(startDate) && order.createdAt.isBefore(endDate)) {
                if (!order.status.equals("cancelled")) {
                    totalOrders++;
                    totalRevenue += order.finalAmount;

                    Customer customer = customers.get(order.customerId);
                    if (customer != null) {
                        report.append("\nOrder ID: ").append(order.id)
                                .append("\nCustomer: ").append(customer.name)
                                .append("\nAmount: $").append(String.format("%.2f", order.finalAmount))
                                .append("\nStatus: ").append(order.status)
                                .append("\n-------------------");
                    }
                }
            }
        }

        report.append("\n\nTotal Orders: ").append(totalOrders)
                .append("\nTotal Revenue: $").append(String.format("%.2f", totalRevenue));
        return report.toString();
    }
}

class Customer {
    public String id;
    public String name;
    public String email;
    public String address;
    public String phone;
    public String membershipLevel;
    public List<Order> orderHistory = new ArrayList<>();
}

class Product {
    public String id;
    public String name;
    public double price;
    public String description;
    public String category;
    public int stock;
    public boolean isActive;
}

class OrderItem {
    public String productId;
    public int quantity;
    public double price;
}

class Order {
    public String id;
    public String customerId;
    public List<OrderItem> items = new ArrayList<>();
    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public double totalAmount;
    public double discountApplied;
    public double finalAmount;
}

class Main {
    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();

        Customer customer = new Customer();
        customer.id = "CUST1";
        customer.name = "John Doe";
        customer.email = "john@example.com";
        customer.address = "123 Main St";
        customer.phone = "555-0123";
        customer.membershipLevel = "gold";

        Product product = new Product();
        product.id = "PROD1";
        product.name = "Widget";
        product.price = 99.99;
        product.description = "A fantastic widget";
        product.category = "gadgets";
        product.stock = 100;
        product.isActive = true;

        processor.addCustomer(customer);
        processor.addProduct(product);

        String orderId = "ORD123";
        Order order = new Order();
        order.id = orderId;
        order.customerId = "CUST1";
        order.status = "pending";
        order.createdAt = LocalDateTime.now();
        order.updatedAt = LocalDateTime.now();
        order.totalAmount = 199.98;

        OrderItem item = new OrderItem();
        item.productId = "PROD1";
        item.quantity = 2;
        item.price = 99.99;
        order.items.add(item);

        processor.orderDatabase.put(orderId, order);
        boolean result = processor.processOrder(orderId);
        System.out.println("Order processing " + (result ? "succeeded" : "failed"));
    }
}
