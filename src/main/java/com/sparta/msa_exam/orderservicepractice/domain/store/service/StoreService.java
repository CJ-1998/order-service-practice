package com.sparta.msa_exam.orderservicepractice.domain.store.service;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.region.repository.RegionRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Category;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.StoreCategory;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.CreateStoreRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateStoreRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateStoreStatusRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.response.StoreInfo;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.CategoryRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.StoreCategoryRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.StoreRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;

import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    @Transactional
    public void createStore(Long userId, @Valid CreateStoreRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Region region = regionRepository.findByName(request.regionName())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));
        Store store = Store.from(request, region, user);
        storeRepository.save(store);

        assert request.categoryName() != null;
        request.categoryName().forEach(categoryName -> {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            storeCategoryRepository.save(new StoreCategory(category, store));
        });
    }

    public StoreInfo getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .map(StoreInfo::from)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
    }

    public Page<StoreInfo> getStores(Pageable pageable, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            Page<Store> storePage = storeRepository.findByKeyword(keyword, pageable);
            List<StoreInfo> storeInfoList = storePage.getContent().stream()
                    .map(StoreInfo::from)
                    .toList();
            return new PageImpl<>(storeInfoList, pageable, storePage.getTotalElements());
        } else {
            Page<Store> storePage = storeRepository.findAll(pageable);
            List<StoreInfo> storeInfoList = storePage.getContent().stream()
                    .map(StoreInfo::from)
                    .toList();
            return new PageImpl<>(storeInfoList, pageable, storePage.getTotalElements());
        }
    }

    @Transactional
    public void updateStore(UUID storeId, @Valid UpdateStoreRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        Region region = regionRepository.findByName(request.regionName())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));
        store.updateInfo(request, region);

        // 기존 카테고리 가져오기
        List<StoreCategory> existingCategories = storeCategoryRepository.findByStore(store);

        // 요청된 카테고리 이름을 Set으로 변환
        Set<String> requestedCategoryNames = new HashSet<>(Optional.ofNullable(request.categoryName()).orElse(Collections.emptyList()));

        // 기존 카테고리 이름을 Set으로 변환
        Set<String> existingCategoryNames = existingCategories.stream()
                .map(sc -> sc.getCategory().getName())
                .collect(Collectors.toSet());

        // 추가할 카테고리 찾기
        requestedCategoryNames.stream()
                .filter(name -> !existingCategoryNames.contains(name))
                .forEach(name -> {
                    Category category = categoryRepository.findByName(name)
                            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
                    storeCategoryRepository.save(new StoreCategory(category, store));
                });

        // 삭제할 카테고리 찾기
        existingCategories.stream()
                .filter(existingCategory -> !requestedCategoryNames.contains(existingCategory.getCategory().getName()))
                .forEach(storeCategoryRepository::delete);
    }

    @Transactional
    public void updateStoreStatus(UUID storeId, @Valid UpdateStoreStatusRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스토어입니다."));
        store.updateStatus(request.storeStatus());
    }

    @Transactional
    public void deleteStore(UUID storeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스토어입니다."));
        store.softDelete(user.getNickname());
    }


}
