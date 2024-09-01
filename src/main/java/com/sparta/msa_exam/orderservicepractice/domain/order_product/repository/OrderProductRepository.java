package com.sparta.msa_exam.orderservicepractice.domain.order_product.repository;

import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
    // Order와 Product를 사용한 OrderProduct 조회
    @Query("SELECT op FROM OrderProduct op WHERE op.order.id = :orderId AND op.product.id = :productId")
    Optional<OrderProduct> findByOrderIdAndProductId(@Param("orderId") UUID orderId, @Param("productId") UUID productId);

    // Order에 속한 모든 OrderProduct 조회
    @Query("SELECT op FROM OrderProduct op WHERE op.order.id = :orderId")
    List<OrderProduct> findByOrderId(@Param("orderId") UUID orderId);

    // Order에 속한 모든 OrderProduct 삭제
    @Modifying
    @Query("DELETE FROM OrderProduct op WHERE op.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") UUID orderId);
}
