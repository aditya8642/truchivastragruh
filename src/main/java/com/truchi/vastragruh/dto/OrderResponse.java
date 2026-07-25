package com.truchi.vastragruh.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Integer orderId,
                            String status,
                            String paymentMethod,
                            BigDecimal totalAmount,
                            ShippingAddressRequest address,
                            List<OrderItemResponse> items,
                            LocalDateTime placedAt,
                            LocalDate estimatedDeliveryDate) {
}
