package com.sparta.msa_exam.orderservicepractice.domain.payment.repository;

import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
