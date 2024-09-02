package com.sparta.msa_exam.orderservicepractice.domain.order.domain.mapper;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDto toOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .storeId(order.getStore().getId())
                .userId(order.getUser().getId())
                .orderAddress(order.getOrderAddress())
                .orderRequest(order.getOrderRequest())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .orderCategory(order.getOrderCategory())
                .totalPrice(order.getTotalPrice())
                .orderProducts(order.getOrderProducts().stream()
                        .map(this::toOrderProductResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct) {
        return OrderProductResponseDto.builder()
                .productId(orderProduct.getProduct().getId())
                .productName(orderProduct.getProduct().getName())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getProduct().getPrice())
                .build();
    }
}
