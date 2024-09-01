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

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("totalPrice")
    private Integer totalPrice;

    @Builder
    public OrderProductResponseDto(UUID orderId, UUID productId, Integer quantity, Integer totalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
