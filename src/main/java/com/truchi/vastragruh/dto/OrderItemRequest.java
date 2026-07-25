package com.truchi.vastragruh.dto;

import java.math.BigDecimal;

public record OrderItemRequest(Long productId,
                               String name,
                               Integer quantity,
                               BigDecimal price) {
}
