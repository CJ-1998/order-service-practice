package com.sparta.msa_exam.orderservicepractice.domain.product.domain.mapper;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequestDto dto) {
        if (dto == null) {
            throw new ServiceException(ErrorCode.NULL_OR_EMPTY_VALUE);
        }
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }

    public ProductResponseDto toProductResponseDto(Product product) {
        if (product == null) {
            throw new ServiceException(ErrorCode.NULL_OR_EMPTY_VALUE);
        }
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .store(product.getStore())
                .build();
    }
}
