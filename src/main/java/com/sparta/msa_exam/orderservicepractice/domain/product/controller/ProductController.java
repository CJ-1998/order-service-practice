package com.sparta.msa_exam.orderservicepractice.domain.product.controller;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.mapper.ProductMapper;
import com.sparta.msa_exam.orderservicepractice.domain.product.service.ProductService;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.PagedResponseDto;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @PostMapping("/stores/{storeId}") // store 에 product 등록
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

    @GetMapping("/stores/{storeId}") // store 에 등록된 product 조회
    public ResponseEntity<ResponseBody<PagedResponseDto<ProductResponseDto>>> getProductsByStoreId(
            @PathVariable UUID storeId,
            @RequestParam(required = false) ProductStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Product> productPage = (status != null)
                ? productService.getProductsByStoreIdAndStatus(storeId, status, pageable)
                : productService.getProductsByStoreId(storeId, pageable);

        Page<ProductResponseDto> responseDtoPage = productPage.map(productMapper::toProductResponseDto);
        PagedResponseDto<ProductResponseDto> pagedResponseDto = new PagedResponseDto<>(responseDtoPage);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(pagedResponseDto));
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
