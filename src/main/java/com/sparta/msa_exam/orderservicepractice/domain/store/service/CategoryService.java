package com.sparta.msa_exam.orderservicepractice.domain.store.service;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Category;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateCategoryRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.response.CategoryInfo;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.CategoryRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.CreateCategoryRequest;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createCategory(@Valid CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("중복되는 카테고리 이름입니다.");
        }
        categoryRepository.save(new Category(request.name()));
    }

    public List<CategoryInfo> getCategories() {
        return categoryRepository.findAll().stream().map(CategoryInfo::from).toList();
    }

    @Transactional
    public void updateCategory(UUID categoryId, @Valid UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 카테고리입니다."));
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("중복되는 카테고리 이름입니다.");
        }
        category.updateName(request.name());
    }

    @Transactional
    public void deleteCategory(UUID categoryId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 카테고리입니다."));
        category.softDelete(user.getNickname());
    }
}
