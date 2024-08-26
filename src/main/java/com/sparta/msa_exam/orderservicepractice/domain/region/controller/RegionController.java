package com.sparta.msa_exam.orderservicepractice.domain.region.controller;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.CreateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.UpdateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.response.RegionInfo;
import com.sparta.msa_exam.orderservicepractice.domain.region.service.RegionService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    @PostMapping
    public ResponseEntity<ResponseBody<Void>> createRegion(@RequestBody @Valid CreateRegionRequest request) {
        Long userId = 1L; // TODO. 수정
        regionService.createRegion(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<RegionInfo>>> getRegions() {
        return ResponseEntity.ok(createSuccessResponse(regionService.getRegions()));
    }

    @PutMapping("/{regionId}")
    public ResponseEntity<ResponseBody<Void>> updateRegion(@PathVariable UUID regionId,
                                                           @RequestBody @Valid UpdateRegionRequest request) {
        Long userId = 1L; // TODO. 수정
        regionService.updateRegion(userId, regionId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping("/{regionId}")
    public ResponseEntity<ResponseBody<Void>> deleteRegion(@PathVariable UUID regionId) {
        Long userId = 1L; // TODO. 수정
        regionService.deleteRegion(userId, regionId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
