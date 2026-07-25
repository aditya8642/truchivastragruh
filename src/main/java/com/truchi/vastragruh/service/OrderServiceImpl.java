package com.truchi.vastragruh.service;

import com.truchi.vastragruh.dto.*;
import com.truchi.vastragruh.entity.Order;
import com.truchi.vastragruh.entity.OrderItem;
import com.truchi.vastragruh.entity.User;
import com.truchi.vastragruh.enums.OrderStatus;
import com.truchi.vastragruh.repository.OrderRepository;
import com.truchi.vastragruh.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int ESTIMATED_DELIVERY_DAYS = 5;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(String userEmail, PlaceOrderRequest request) {
        User user = getUser(userEmail);

        // The UI generates a random 5-digit id, so a collision (however rare)
        // is possible - regenerate on the backend rather than silently
        // failing or overwriting someone else's order.
        Integer orderNumber = request.orderId();
        while (orderNumber == null || orderRepository.existsByOrderNumber(orderNumber)) {
            orderNumber = 10000 + (int) (Math.random() * 90000);
        }

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUser(user);
        order.setPaymentMethod(request.paymentMethod());
        order.setTotalAmount(request.totalAmount());
        order.setStatus(OrderStatus.BOOKED);
        order.setEstimatedDeliveryDate(LocalDate.now().plusDays(ESTIMATED_DELIVERY_DAYS));
        order.setShippingAddress(toEmbeddedAddress(request.address()));

        List<OrderItem> items = request.items().stream().map(itemReq -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemReq.productId());
            item.setProductName(itemReq.name());
            item.setQuantity(itemReq.quantity());
            item.setPrice(itemReq.price());
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> getOrdersForUser(String userEmail) {
        User user = getUser(userEmail);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(String userEmail, Integer orderNumber) {
        User user = getUser(userEmail);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Integer orderNumber, String newStatus) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(OrderStatus.valueOf(newStatus.toUpperCase()));
        return toResponse(orderRepository.save(order));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private ShippingAddress  toEmbeddedAddress(ShippingAddressRequest req) {
        ShippingAddress addr = new ShippingAddress();
        addr.setFullName(req.fullName());
        addr.setPhone(req.phone());
        addr.setLine1(req.line1());
        addr.setLine2(req.line2());
        addr.setCity(req.city());
        addr.setState(req.state());
        addr.setPincode(req.pincode());
        addr.setType(req.type());
        return addr;
    }

    private OrderResponse toResponse(Order order) {
        ShippingAddress a = order.getShippingAddress();
        ShippingAddressRequest addressDto = new ShippingAddressRequest(
                a.getFullName(), a.getPhone(), a.getLine1(), a.getLine2(),
                a.getCity(), a.getState(), a.getPincode(), a.getType()
        );

        List<OrderItemResponse> itemDtos = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProductId(), i.getProductName(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getPaymentMethod(),
                order.getTotalAmount(),
                addressDto,
                itemDtos,
                order.getCreatedAt(),
                order.getEstimatedDeliveryDate()
        );
    }
}
