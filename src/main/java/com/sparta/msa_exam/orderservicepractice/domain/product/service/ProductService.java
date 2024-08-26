package com.sparta.msa_exam.orderservicepractice.domain.product.service;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.repository.ProductRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
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
