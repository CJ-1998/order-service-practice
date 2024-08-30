package com.sparta.msa_exam.orderservicepractice.domain.payment.repository;

import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.Payment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByUserUsername(String username);
}
