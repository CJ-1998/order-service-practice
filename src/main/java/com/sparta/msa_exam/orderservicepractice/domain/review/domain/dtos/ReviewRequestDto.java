package com.sparta.msa_exam.orderservicepractice.domain.review.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReviewRequestDto {
    @NotNull
    private UUID storeId;

    @NotNull
    private Long userId;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comment;
}
