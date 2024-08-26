package com.sparta.msa_exam.orderservicepractice.domain.order_product.repository;

import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
    Optional<OrderProduct> findByOrderIdAndProductId(UUID orderId, UUID productId);
    List<OrderProduct> findByOrderId(UUID orderId);
    @Modifying
    @Query("DELETE FROM OrderProduct op WHERE op.order.id = :orderId")
    void deleteByOrderId(UUID orderId);
}
