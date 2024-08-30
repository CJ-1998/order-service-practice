package com.sparta.msa_exam.orderservicepractice.domain.payment.dto;

import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.Payment;
import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private int paymentAmount;
    private LocalDateTime paymentAt;
    private UUID transactionId;
    private PaymentStatus paymentStatus;
    private String userName;

    public static PaymentResponseDto convertToPaymentResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentAmount(payment.getPaymentAmount())
                .paymentAt(payment.getPaymentAt())
                .transactionId(payment.getTransactionId())
                .paymentStatus(payment.getPaymentStatus())
                .userName(payment.getUser().getUsername())
                .build();
    }
}
