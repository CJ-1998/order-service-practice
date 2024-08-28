package com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateStoreRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String regionName,
        @Nullable List<String> categoryName
) {
}
