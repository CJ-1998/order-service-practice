package com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderCategory;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    private UUID storeId;
    private String orderAddress;
    private String orderRequest;
    private OrderCategory orderCategory;
    private List<OrderProductRequestDto> orderProducts;

    @Builder
    public OrderRequestDto(UUID storeId, String orderAddress, String orderRequest, OrderCategory orderCategory, List<OrderProductRequestDto> orderProducts) {
        this.storeId = storeId;
        this.orderAddress = orderAddress;
        this.orderRequest = orderRequest;
        this.orderCategory = orderCategory;
        this.orderProducts = orderProducts;
    }

    public boolean hasStoreId() {
        return storeId != null;
    }

    public boolean hasOrderAddress() {
        return orderAddress != null && !orderAddress.isEmpty();
    }

    public boolean hasOrderRequest() {
        return orderRequest != null && !orderRequest.isEmpty();
    }

    public boolean hasOrderCategory() {
        return orderCategory != null;
    }

    public boolean hasOrderProducts() {
        return orderProducts != null && !orderProducts.isEmpty();
    }
}
