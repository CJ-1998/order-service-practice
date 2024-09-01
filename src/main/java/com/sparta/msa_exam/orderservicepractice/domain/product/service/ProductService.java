package com.sparta.msa_exam.orderservicepractice.domain.product.service;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos.ProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.repository.ProductRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.StoreRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Product createProduct(@Valid ProductRequestDto productRequestDto, UUID storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .store(store)
                .build();

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByStoreId(UUID storeId) {
        return productRepository.findAllByStoreId(storeId);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByStoreIdAndStatus(UUID storeId, ProductStatus status) {
        return productRepository.findAllByStoreIdAndStatus(storeId, status);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByStoreId(UUID storeId, Pageable pageable) {
        return productRepository.findAllByStoreId(storeId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByStoreIdAndStatus(UUID storeId, ProductStatus status, Pageable pageable) {
        return productRepository.findAllByStoreIdAndStatus(storeId, status, pageable);
    }

    @Transactional
    public Product updateProduct(UUID productId, Product productToUpdate) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.updateDetails(productToUpdate);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public Product hideProduct(UUID productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.hide();
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public Product deleteProduct(UUID productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.delete();
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }
}
