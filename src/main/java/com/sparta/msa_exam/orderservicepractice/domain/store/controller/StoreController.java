package com.sparta.msa_exam.orderservicepractice.domain.store.controller;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.CreateStoreRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateStoreRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateStoreStatusRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.response.StoreInfo;
import com.sparta.msa_exam.orderservicepractice.domain.store.service.StoreService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<ResponseBody<Void>> createStore(@RequestBody @Valid CreateStoreRequest request) {
        Long userId = 1L;
        storeService.createStore(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ResponseBody<StoreInfo>> getStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(createSuccessResponse(storeService.getStore(storeId)));
    }

    @GetMapping
    public ResponseEntity<ResponseBody<Page<StoreInfo>>> getStores(Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(storeService.getStores(pageable)));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<ResponseBody<Void>> updateStore(@PathVariable UUID storeId,
                                                          @RequestBody @Valid UpdateStoreRequest request) {
        storeService.updateStore(storeId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<ResponseBody<Void>> updateStoreStatus(@PathVariable UUID storeId,
                                                                @RequestBody @Valid UpdateStoreStatusRequest request) {
        storeService.updateStoreStatus(storeId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<ResponseBody<Void>> deleteStore(@PathVariable UUID storeId) {
        Long userId = 1L;
        storeService.deleteStore(storeId, userId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
