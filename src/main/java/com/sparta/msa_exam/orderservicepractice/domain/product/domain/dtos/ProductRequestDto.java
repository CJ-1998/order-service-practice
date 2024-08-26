package com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class ProductRequestDto {
    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 10, max = 500)
    private String description;

    @NotNull
    private Integer price;
}
