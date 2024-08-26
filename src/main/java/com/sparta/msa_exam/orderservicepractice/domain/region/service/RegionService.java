package com.sparta.msa_exam.orderservicepractice.domain.region.service;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.CreateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.UpdateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.response.RegionInfo;
import com.sparta.msa_exam.orderservicepractice.domain.region.repository.RegionRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode.REGION_ALREADY_EXISTS;
import static com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode.REGION_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RegionService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public void createRegion(Long userId, @Valid CreateRegionRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (regionRepository.existsByName(request.name())) {
            throw new ServiceException(REGION_ALREADY_EXISTS);
        }
        regionRepository.save(Region.from(request));
    }

    public List<RegionInfo> getRegions() {
        return regionRepository.findAll().stream().map(RegionInfo::from).toList();
    }

    @Transactional
    public void updateRegion(Long userId, UUID regionId, @Valid UpdateRegionRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Region region = regionRepository.findById(regionId).orElseThrow(() -> new ServiceException(REGION_NOT_FOUND));
        if (regionRepository.existsByName(request.name())) {
            throw new ServiceException(REGION_ALREADY_EXISTS);
        }
        region.updateName(request.name());
    }

    @Transactional
    public void deleteRegion(Long userId, UUID regionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Region region = regionRepository.findById(regionId).orElseThrow(() -> new ServiceException(REGION_NOT_FOUND));
        region.softDelete(user.getNickname());
    }
}
