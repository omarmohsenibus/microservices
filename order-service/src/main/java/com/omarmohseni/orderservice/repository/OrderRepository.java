package com.omarmohseni.orderservice.repository;

import com.omarmohseni.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
