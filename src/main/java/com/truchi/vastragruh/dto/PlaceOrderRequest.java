package com.truchi.vastragruh.dto;

import java.math.BigDecimal;
import java.util.List;

public record PlaceOrderRequest(Integer orderId, // the UI-generated 5-digit id
                                List<OrderItemRequest> items,
                                ShippingAddressRequest address,
                                String paymentMethod,
                                BigDecimal totalAmount) {
}
