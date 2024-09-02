package com.sparta.msa_exam.orderservicepractice.domain.order.domain;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderCategory;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "order_address", nullable = false)
    private String orderAddress;

    @Column(name = "order_request", nullable = false)
    private String orderRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_category", nullable = false)
    private OrderCategory orderCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Column(name = "total_price")
    private Integer totalPrice = 0;


    @Builder
    public Order(UUID id, OrderStatus orderStatus, String orderAddress, String orderRequest, PaymentStatus paymentStatus,
                 OrderCategory orderCategory, User user, Store store, List<OrderProduct> orderProducts) {
        this.id = id;
        this.orderStatus = orderStatus != null ? orderStatus : OrderStatus.PENDING;
        this.orderAddress = orderAddress;
        this.orderRequest = orderRequest;
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PENDING;
        this.orderCategory = orderCategory;
        this.user = user;
        this.store = store;
        this.orderProducts = orderProducts != null ? new ArrayList<>(orderProducts) : new ArrayList<>();
        this.updateTotalPrice();
    }

    public void updateOrderAddress(String orderAddress) {
        if (orderAddress != null && !orderAddress.isEmpty()) {
            this.orderAddress = orderAddress;
        }
    }

    public void updateOrderRequest(String orderRequest) {
        if (orderRequest != null && !orderRequest.isEmpty()) {
            this.orderRequest = orderRequest;
        }
    }

    public void updateOrderCategory(OrderCategory orderCategory) {
        if (orderCategory != null) {
            this.orderCategory = orderCategory;
        }
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if (orderStatus != null) {
            this.orderStatus = orderStatus;
        }
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        if (paymentStatus != null) {
            this.paymentStatus = paymentStatus;
        }
    }

    public void updateTotalPrice() {
        this.totalPrice = this.orderProducts.stream()
                .mapToInt(orderProduct -> orderProduct.getProduct().getPrice() * orderProduct.getQuantity())
                .sum();
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
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void updateOrderProducts(List<OrderProduct> newOrderProducts) {
        orderProducts.clear();
        if (newOrderProducts != null) {
            for (OrderProduct orderProduct : newOrderProducts) {
                addOrderProduct(orderProduct);
            }
        }
        updateTotalPrice();
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        orderProducts.remove(orderProduct);
        orderProduct.setOrder(null);
    }
}
