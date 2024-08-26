package com.sparta.msa_exam.orderservicepractice.domain.order.domain;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderCategory;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "order_address", nullable = false)
    private String orderAddress;

    @Column(name = "order_request", nullable = false)
    private String orderRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_category", nullable = false)
    private OrderCategory orderCategory;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Order(UUID id, int totalPrice, String orderAddress, String orderRequest, PaymentStatus paymentStatus,
                 OrderCategory orderCategory, OrderStatus orderStatus, UUID userId, UUID storeId, List<OrderProduct> orderProducts) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderAddress = orderAddress;
        this.orderRequest = orderRequest;
        this.paymentStatus = paymentStatus;
        this.orderCategory = orderCategory;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.storeId = storeId;
        if (orderProducts != null) {
            this.orderProducts = orderProducts;
        }
    }

    public void updateUserId(UUID userId) {
        this.userId = userId;
    }

    public void updateStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public void updateDetails(Order updatedOrder) {
        this.totalPrice = updatedOrder.getTotalPrice();
        this.orderAddress = updatedOrder.getOrderAddress();
        this.orderRequest = updatedOrder.getOrderRequest();
        this.paymentStatus = updatedOrder.getPaymentStatus();
        this.orderCategory = updatedOrder.getOrderCategory();
        this.orderStatus = updatedOrder.getOrderStatus();
    }

    public void updateTotalPrice() {
        this.totalPrice = orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getPrice() * orderProduct.getQuantity())
                .reduce(0, Integer::sum);
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void deleteOrder() {
        this.orderStatus = OrderStatus.DELETED;
    }

    public void softDelete(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.DELETED;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        if (orderProducts.remove(orderProduct)) {
            orderProduct.setOrder(null); // Unlink the OrderProduct from this Order
        }
    }
}
