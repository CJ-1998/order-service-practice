package com.sparta.msa_exam.orderservicepractice.domain.payment.controller;

import com.sparta.msa_exam.orderservicepractice.domain.payment.dto.PaymentRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.dto.PaymentResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.service.PaymentService;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;
import java.nio.file.AccessDeniedException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Payment 관련 log")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ResponseBody<PaymentResponseDto>> requestPayment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponseDto = paymentService.requestPayment(userDetails, paymentRequestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(paymentResponseDto));
    }

    @DeleteMapping("/{payment_id}")
    public ResponseEntity<ResponseBody<PaymentResponseDto>> deletePayment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("payment_id") UUID paymentId) throws AccessDeniedException {
        PaymentResponseDto paymentResponseDto = paymentService.deletePayment(userDetails, paymentId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(paymentResponseDto));
    }
}
