package com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record CreateStoreRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String regionName,
        @Nullable List<String> categoryName
) {
}
