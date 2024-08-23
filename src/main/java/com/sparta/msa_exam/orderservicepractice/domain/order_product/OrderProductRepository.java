package com.sparta.msa_exam.orderservicepractice.domain.order_product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}
