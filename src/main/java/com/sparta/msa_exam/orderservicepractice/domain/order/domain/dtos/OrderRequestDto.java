package com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderCategory;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderRequestDto {
    private int totalPrice;
    private UUID storeId;
    private String orderAddress;
    private String orderRequest;
    private OrderCategory orderCategory;
    private List<OrderProductRequestDto> products;
}
