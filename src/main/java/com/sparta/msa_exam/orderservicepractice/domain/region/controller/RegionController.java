package com.sparta.msa_exam.orderservicepractice.domain.region.controller;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.CreateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.UpdateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.response.RegionInfo;
import com.sparta.msa_exam.orderservicepractice.domain.region.service.RegionService;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping
    public ResponseEntity<ResponseBody<Void>> createRegion(@RequestBody @Valid CreateRegionRequest request,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        regionService.createRegion(userDetails.getUser().getId(), request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<RegionInfo>>> getRegions() {
        return ResponseEntity.ok(createSuccessResponse(regionService.getRegions()));
    }

    @Secured(UserRole.Authority.ADMIN)
    @PutMapping("/{regionId}")
    public ResponseEntity<ResponseBody<Void>> updateRegion(@PathVariable UUID regionId,
                                                           @RequestBody @Valid UpdateRegionRequest request,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        regionService.updateRegion(userDetails.getUser().getId(), regionId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @Secured(UserRole.Authority.ADMIN)
    @DeleteMapping("/{regionId}")
    public ResponseEntity<ResponseBody<Void>> deleteRegion(@PathVariable UUID regionId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        regionService.deleteRegion(userDetails.getUser().getId(), regionId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
