package com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryInfo(
        UUID id,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        String createdBy,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,
        String updatedBy
) {
    public static CategoryInfo from(Category category) {
        return new CategoryInfo(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getCreatedBy(),
                category.getUpdatedAt(),
                category.getUpdatedBy()
        );
    }
}
