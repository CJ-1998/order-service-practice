package com.sparta.msa_exam.orderservicepractice.domain.payment.service;

import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.Payment;
import com.sparta.msa_exam.orderservicepractice.domain.payment.dto.PaymentRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.dto.PaymentResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.repository.PaymentRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import jakarta.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;
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

    public PaymentResponseDto deletePayment(UserDetailsImpl userDetails, UUID paymentId) throws AccessDeniedException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        if (checkRole(userDetails, payment.getUser())) {
            if (PGRefundRequest(payment.getTransactionId())) {
                payment.softDelete(userDetails.getUsername());
            } else {

                throw new ServiceException(ErrorCode.PAYMENT_REFUND_FAILED);
            }
        }

        return PaymentResponseDto.convertToPaymentResponseDto(payment);
    }

    private boolean PGRefundRequest(UUID transactionId) {
        // PG사에서 transactionId로 찾아서 결제 취소하는 로직
        return true;
    }

    public PaymentResponseDto getPayment(UserDetailsImpl userDetails, UUID paymentId) throws AccessDeniedException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        if (checkRole(userDetails, payment.getUser())) {
            return PaymentResponseDto.convertToPaymentResponseDto(payment);
        }

        throw new AccessDeniedException("권한이 없습니다.");
    }

    private static boolean checkRole(UserDetailsImpl userDetails, User user) throws AccessDeniedException {
        String deletedBy = userDetails.getUsername();

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(UserRole.ADMIN.getAuthority()));

        // 관리자는 누구나 삭제 가능하고, 관리자가 아니면 본인만 삭제 가능
        if (isAdmin || deletedBy.equals(user.getUsername())) {
            return true;
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public List<PaymentResponseDto> getPayments(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        List<Payment> payments = paymentRepository.findByUserUsername(username);

        return payments.stream().map(PaymentResponseDto::convertToPaymentResponseDto).toList();
    }
}
