package com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderCategory;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public class OrderResponseDto {
    private UUID orderId;
    private int totalPrice;
    private String orderAddress;
    private String orderRequest;
    private PaymentStatus paymentStatus;
    private OrderCategory orderCategory;
    private OrderStatus orderStatus;
    private Long userId;
    private UUID storeId;
    private List<OrderProductResponseDto> products;

}
