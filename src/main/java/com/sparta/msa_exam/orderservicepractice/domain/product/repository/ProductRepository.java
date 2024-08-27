package com.sparta.msa_exam.orderservicepractice.domain.product.repository;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByStoreId(UUID storeId);
    List<Product> findAllByStoreIdAndStatus(UUID storeId, ProductStatus status);

    Page<Product> findAllByStoreId(UUID storeId, Pageable pageable);
    Page<Product> findAllByStoreIdAndStatus(UUID storeId, ProductStatus status, Pageable pageable);
}
