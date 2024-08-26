package com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos;

import java.util.UUID;
import lombok.Builder;

@Builder
public class OrderProductResponseDto {
    private UUID productId;
    private Integer quantity;
}
