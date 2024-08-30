package com.sparta.msa_exam.orderservicepractice.domain.order.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(Integer totalPrice, String orderAddress, String orderRequest,
                  OrderCategory orderCategory, OrderStatus orderStatus, User user, Store store, List<OrderProduct> orderProducts) {
        this.totalPrice = totalPrice;
        this.orderAddress = orderAddress;
        this.orderRequest = orderRequest;
        this.paymentStatus = PaymentStatus.PENDING;
        this.orderCategory = orderCategory;
        this.orderStatus = orderStatus;
        this.user = user;
        this.store = store;
        if (orderProducts != null) {
            this.orderProducts = orderProducts;
        }
    }

    public void updateDetails(Order updatedOrder) {
        this.totalPrice = updatedOrder.getTotalPrice();
        this.orderAddress = updatedOrder.getOrderAddress();
        this.orderRequest = updatedOrder.getOrderRequest();
        this.paymentStatus = updatedOrder.getPaymentStatus();
        this.orderCategory = updatedOrder.getOrderCategory();
        this.orderStatus = updatedOrder.getOrderStatus();
        this.user = updatedOrder.getUser();
        this.store = updatedOrder.getStore();
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
            orderProduct.setOrder(null);
        }
    }
}
