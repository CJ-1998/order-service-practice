package com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.vo.StoreStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateStoreStatusRequest(
        @NotNull StoreStatus storeStatus
) {
}
