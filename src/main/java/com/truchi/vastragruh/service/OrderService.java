package com.truchi.vastragruh.service;

import com.truchi.vastragruh.dto.OrderResponse;
import com.truchi.vastragruh.dto.PlaceOrderRequest;

import java.util.List;

public interface OrderService {
    public OrderResponse placeOrder(String userEmail, PlaceOrderRequest request);
    public List<OrderResponse> getOrdersForUser(String userEmail);
    public OrderResponse getOrderById(String userEmail, Integer orderNumber);
    public OrderResponse updateStatus(Integer orderNumber, String newStatus);


}
