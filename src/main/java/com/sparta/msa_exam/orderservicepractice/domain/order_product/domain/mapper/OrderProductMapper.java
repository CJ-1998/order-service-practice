package com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.mapper;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderProductMapper {

    public OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct) {
        return OrderProductResponseDto.builder()
                .orderId(orderProduct.getOrder().getId())
                .productId(orderProduct.getProduct().getId())
                .quantity(orderProduct.getQuantity())
                .totalPrice(orderProduct.getProduct().getPrice() * orderProduct.getQuantity())
                .build();
    }

    public OrderProductResponseDto toOrderProductResponseDto(Order order, OrderProductRequestDto productRequest) {
        UUID productId = productRequest.getProductId();
        OrderProduct orderProduct = order.getOrderProducts().stream()
                .filter(op -> op.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in order"));

        return OrderProductResponseDto.builder()
                .orderId(order.getId())
                .productId(orderProduct.getProduct().getId())
                .quantity(orderProduct.getQuantity())
                .totalPrice(orderProduct.getProduct().getPrice() * orderProduct.getQuantity())
                .build();
    }

    public List<OrderProductResponseDto> toOrderProductResponseDtoList(Order order) {
        return order.getOrderProducts().stream()
                .map(this::toOrderProductResponseDto)
                .collect(Collectors.toList());
    }
}