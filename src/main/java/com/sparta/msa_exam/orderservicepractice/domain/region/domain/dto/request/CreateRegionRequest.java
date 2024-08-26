package com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateRegionRequest(
        @NotBlank String name
) {
}
