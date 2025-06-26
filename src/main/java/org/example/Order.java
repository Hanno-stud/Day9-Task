package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    public String orderId;
    public String userEmail;
    public List<String> productList;
    public double totalAmount;
    public String status; // e.g., Placed, Shipped, Delivered, Cancelled
    public LocalDateTime orderDate;
}