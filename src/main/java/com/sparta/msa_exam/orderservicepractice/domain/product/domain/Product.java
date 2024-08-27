package com.sparta.msa_exam.orderservicepractice.domain.product.domain;

import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Product(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = ProductStatus.ACTIVE;
    }

    public void updateStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public void updateDetails(Product updatedProduct) {
        this.name = updatedProduct.getName();
        this.description = updatedProduct.getDescription();
        this.price = updatedProduct.getPrice();
        this.status = updatedProduct.getStatus();
        this.storeId = updatedProduct.getStoreId();
    }

    public void hide() {
        this.status = ProductStatus.HIDDEN;
    }

    public void delete() {
        this.status = ProductStatus.DELETED;
    }

    public void softDelete(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        this.status = ProductStatus.DELETED;
    }
}
