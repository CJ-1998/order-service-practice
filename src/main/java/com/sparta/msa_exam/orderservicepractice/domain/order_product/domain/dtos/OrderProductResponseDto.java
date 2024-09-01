package com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값을 가지는 필드는 제외
public class OrderProductResponseDto {

    @JsonProperty("orderId")
    private UUID orderId;

    @JsonProperty("productId")
    private UUID productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("totalPrice")
    private Integer totalPrice;

    @Builder
    public OrderProductResponseDto(UUID orderId, UUID productId, String productName, Integer quantity, Integer price, Integer totalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }
}
