package com.sparta.msa_exam.orderservicepractice.domain.product.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.enums.ProductStatus;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값을 가지는 필드는 제외
public class ProductResponseDto {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("status")
    private ProductStatus status;

    @JsonProperty("storeId")
    private UUID storeId;

    @Builder
    public ProductResponseDto(UUID id, String name, String description, Integer price, ProductStatus status, Store store) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.storeId  = store.getId();
    }
}
