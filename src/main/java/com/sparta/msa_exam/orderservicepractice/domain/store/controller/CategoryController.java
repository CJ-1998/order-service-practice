package com.sparta.msa_exam.orderservicepractice.domain.store.controller;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.CreateCategoryRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateCategoryRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.response.CategoryInfo;
import com.sparta.msa_exam.orderservicepractice.domain.store.service.CategoryService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping
    public ResponseEntity<ResponseBody<Void>> createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<CategoryInfo>>> getCategories() {
        return ResponseEntity.ok(createSuccessResponse(categoryService.getCategories()));
    }

    @Secured(UserRole.Authority.ADMIN)
    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseBody<Void>> updateCategory(@PathVariable UUID categoryId,
                                                             @RequestBody @Valid UpdateCategoryRequest request) {
        categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @Secured(UserRole.Authority.ADMIN)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseBody<Void>> deleteCategory(@PathVariable UUID categoryId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoryService.deleteCategory(categoryId, userDetails.getUser().getId());
        return ResponseEntity.ok(createSuccessResponse());
    }
}
