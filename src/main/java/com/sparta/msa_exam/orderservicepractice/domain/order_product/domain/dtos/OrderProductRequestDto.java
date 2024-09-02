package com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos;

import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderProductRequestDto {
    private UUID productId;
    private Integer quantity;
}
