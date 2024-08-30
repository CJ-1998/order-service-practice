package com.sparta.msa_exam.orderservicepractice.domain.order.repository;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUserId(Long userId);
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    Page<Order> findAllByUserId(Long userId, Pageable pageable);
    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
