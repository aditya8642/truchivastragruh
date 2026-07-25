package com.truchi.vastragruh.controller;

import com.truchi.vastragruh.dto.OrderResponse;
import com.truchi.vastragruh.dto.PlaceOrderRequest;
import com.truchi.vastragruh.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(Authentication authentication,
                                                    @RequestBody PlaceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(authentication.getName(), request));
    }

    // "My orders" - what the user sees after logging in.
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrdersForUser(authentication.getName()));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrder(Authentication authentication,
                                                  @PathVariable Integer orderNumber) {
        return ResponseEntity.ok(orderService.getOrderById(authentication.getName(), orderNumber));
    }

    // NOTE: this should be admin-only (a customer shouldn't be able to mark
    // their own order "DELIVERED"), but your SecurityConfig's admin matcher
    // currently has a typo ("/acategory-grid.tspi/v1/admin/**") that makes it
    // match nothing - so right now this only requires being logged in as
    // *someone*, not an admin. Flagging rather than silently shipping a
    // security hole - say the word and I'll fix that matcher plus add
    // @PreAuthorize here properly.
    /*@PatchMapping("/{orderNumber}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Integer orderNumber,
                                                      @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(orderNumber, request.status()));
    }*/
}
