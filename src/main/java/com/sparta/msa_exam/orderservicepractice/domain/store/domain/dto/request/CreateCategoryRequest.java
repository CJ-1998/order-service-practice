package com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank String name
) {
}
