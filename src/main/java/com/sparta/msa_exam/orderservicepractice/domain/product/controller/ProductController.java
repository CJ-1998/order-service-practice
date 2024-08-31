package com.sparta.msa_exam.orderservicepractice.domain.product.controller;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.mapper.ProductMapper;
import com.sparta.msa_exam.orderservicepractice.domain.product.service.ProductService;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @PostMapping("/{storeId}") // store 에 product 등록
    public ResponseEntity<ResponseBody<ProductResponseDto>> createProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto,
            @PathVariable UUID storeId) {

        Product createdProduct = productService.createProduct(productRequestDto, storeId);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(createdProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @GetMapping("/{productId}") // product 단건 조회
    public ResponseEntity<ResponseBody<ProductResponseDto>> getProductById(@PathVariable UUID productId) {
        Product foundProduct = productService.getProductById(productId);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(foundProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @GetMapping("/stores/{storeId}") // store에 등록된 product 목록 조회
    public ResponseEntity<ResponseBody<Page<ProductResponseDto>>> getProductsByStoreId(
            @PathVariable UUID storeId,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        // 페이지 크기 제한: 10, 30, 50 이외의 값은 10으로 고정
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        // 정렬 방향 처리
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Product> productPage;

        if (status != null) {
            productPage = productService.getProductsByStoreIdAndStatus(storeId, status, pageable);
        } else {
            productPage = productService.getProductsByStoreId(storeId, pageable);
        }

        Page<ProductResponseDto> responseDtos = productPage.map(productMapper::toProductResponseDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @PutMapping("/{productId}") // product 수정
    public ResponseEntity<ResponseBody<ProductResponseDto>> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProductRequestDto productRequestDto) {

        Product productToUpdate = productMapper.toProduct(productRequestDto);
        Product updatedProduct = productService.updateProduct(productId, productToUpdate);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(updatedProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @PatchMapping("/{productId}/hide") // product 숨김
    public ResponseEntity<ResponseBody<ProductResponseDto>> hideProduct(@PathVariable UUID productId) {
        Product hiddenProduct = productService.hideProduct(productId);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(hiddenProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @DeleteMapping("/{productId}") // product 삭제
    public ResponseEntity<ResponseBody<ProductResponseDto>> deleteProduct(@PathVariable UUID productId) {
        Product deletedProduct = productService.deleteProduct(productId);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(deletedProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }
}
