package com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegionInfo(
        UUID id,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        String createdBy,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,
        String updatedBy
) {
    public static RegionInfo from(Region region) {
        return new RegionInfo(
                region.getId(),
                region.getName(),
                region.getCreatedAt(),
                region.getCreatedBy(),
                region.getUpdatedAt(),
                region.getUpdatedBy()
        );
    }
}
