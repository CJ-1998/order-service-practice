package com.sparta.msa_exam.orderservicepractice.domain.payment.service;

import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.Payment;
import com.sparta.msa_exam.orderservicepractice.domain.payment.dto.PaymentRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.dto.PaymentResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.repository.PaymentRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponseDto requestPayment(UserDetailsImpl userDetails, PaymentRequestDto paymentRequestDto) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        Payment payment = Payment.createPayment(user, paymentRequestDto.getPaymentAmount());

        payment.successPayment(PGRequest());

        paymentRepository.save(payment);

        return PaymentResponseDto.convertToPaymentResponseDto(payment);
    }

    private UUID PGRequest() {
        return UUID.randomUUID();
    }
}
