package com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderCategory;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값을 가지는 필드는 제외
public class OrderResponseDto {
    @JsonProperty("orderId")
    private UUID orderId;

    @JsonProperty("storeId")
    private UUID storeId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("orderAddress")
    private String orderAddress;

    @JsonProperty("orderRequest")
    private String orderRequest;

    @JsonProperty("orderStatus")
    private OrderStatus orderStatus;

    @JsonProperty("paymentStatus")
    private PaymentStatus paymentStatus;

    @JsonProperty("orderCategory")
    private OrderCategory orderCategory;

    @JsonProperty("totalPrice")
    private Integer totalPrice;

    @JsonProperty("orderProducts")
    private List<OrderProductResponseDto> orderProducts;

    @Builder
    public OrderResponseDto(UUID orderId, UUID storeId, Long userId, String orderAddress, String orderRequest,
                            OrderStatus orderStatus, PaymentStatus paymentStatus, OrderCategory orderCategory,
                            Integer totalPrice, List<OrderProductResponseDto> orderProducts) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.userId = userId;
        this.orderAddress = orderAddress;
        this.orderRequest = orderRequest;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderCategory = orderCategory;
        this.totalPrice = totalPrice;
        this.orderProducts = orderProducts;
    }
}
