package com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Integer price;
    private ProductStatus status;
    private Store store;
}
