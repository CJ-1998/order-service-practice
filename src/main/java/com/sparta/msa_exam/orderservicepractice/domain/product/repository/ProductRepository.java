package com.sparta.msa_exam.orderservicepractice.domain.product.repository;

import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
