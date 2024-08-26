package com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateRegionRequest(
        @NotBlank String name
) {
}
