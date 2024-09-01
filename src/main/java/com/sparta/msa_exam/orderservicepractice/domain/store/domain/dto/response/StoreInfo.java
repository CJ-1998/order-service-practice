package com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.vo.StoreStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StoreInfo(
        UUID id,
        String name,
        String description,
        String address,
        StoreStatus status,
        String regionName,
        List<String> categoryName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        String createdBy,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,
        String updatedBy
) {
    public static StoreInfo from(Store store) {
        return new StoreInfo(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getAddress(),
                store.getStatus(),
                store.getRegion().getName(),
                store.getStoreCategories().stream()
                        .map(storeCategory -> storeCategory.getCategory().getName()).toList(),
                store.getCreatedAt(),
                store.getCreatedBy(),
                store.getUpdatedAt(),
                store.getUpdatedBy()
        );
    }
}
