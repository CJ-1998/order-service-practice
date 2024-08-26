package com.sparta.msa_exam.orderservicepractice.domain.product.domain.controller;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.mapper.ProductMapper;
import com.sparta.msa_exam.orderservicepractice.domain.product.service.ProductService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/{storeId}") // store 에 product 등록
    public ResponseEntity<ResponseBody<ProductResponseDto>> createProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto,
            @PathVariable UUID storeId) {

        Product product = productMapper.toProduct(productRequestDto);
        product.updateStoreId(storeId);

        Product createdProduct = productService.createProduct(product);
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
    public ResponseEntity<ResponseBody<List<ProductResponseDto>>> getProductsByStoreId(
            @PathVariable UUID storeId,
            @RequestParam(required = false) ProductStatus status) {

        List<Product> products;
        if (status != null) {
            products = productService.getProductsByStoreIdAndStatus(storeId, status);
        } else {
            products = productService.getProductsByStoreId(storeId);
        }

        List<ProductResponseDto> responseDtos = products.stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @PutMapping("/{productId}") // product 수정
    public ResponseEntity<ResponseBody<ProductResponseDto>> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProductRequestDto productRequestDto) {

        Product productToUpdate = productMapper.toProduct(productRequestDto);
        Product updatedProduct = productService.updateProduct(productId, productToUpdate);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(updatedProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PatchMapping("/{productId}/hide") // product 숨김
    public ResponseEntity<ResponseBody<ProductResponseDto>> hideProduct(@PathVariable UUID productId) {
        Product hiddenProduct = productService.hideProduct(productId);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(hiddenProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @DeleteMapping("/{productId}") // product 삭제
    public ResponseEntity<ResponseBody<ProductResponseDto>> deleteProduct(@PathVariable UUID productId) {
        Product deletedProduct = productService.deleteProduct(productId);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(deletedProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }
}
