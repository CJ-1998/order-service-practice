package com.sparta.msa_exam.orderservicepractice.domain.payment.domain;

import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "payment_amount", nullable = false)
    private int paymentAmount;

    @Column(name = "payment_at", nullable = false)
    private LocalDateTime paymentAt;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Payment(int paymentAmount, LocalDateTime paymentAt, UUID transactionId, PaymentStatus paymentStatus,
                   User user) {
        this.paymentAmount = paymentAmount;
        this.paymentAt = paymentAt;
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.user = user;
    }

    public static Payment createPayment(User user, int paymentAmount) {
        return new Payment(paymentAmount, LocalDateTime.now(), null, PaymentStatus.PENDING, user);
    }

    public void successPayment(UUID transactionId) {
        this.transactionId = transactionId;
        this.paymentStatus = PaymentStatus.PAID;
    }

    public void softDelete(String deleteBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }
}
