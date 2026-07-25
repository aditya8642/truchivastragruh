package com.truchi.vastragruh.repository;

import com.truchi.vastragruh.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Scoped to (id AND userId) together, same reasoning as AddressRepository -
    // stops one logged-in user from viewing another user's order by guessing an id.
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    boolean existsByOrderNumber(Integer orderNumber);

    java.util.Optional<Order> findByOrderNumber(Integer orderNumber);
}
