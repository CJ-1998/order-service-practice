package com.sparta.msa_exam.orderservicepractice.domain.order.domain.mapper;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.repository.ProductRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(OrderRequestDto dto) {
        if (dto == null) {
            throw new ServiceException(ErrorCode.NULL_OR_EMPTY_VALUE);
        }

        Order order = Order.builder()
                .orderAddress(dto.getOrderAddress())
                .orderRequest(dto.getOrderRequest())
                .orderCategory(dto.getOrderCategory())
                .build();

        return order;
    }

    public OrderResponseDto toOrderResponseDto(Order order) {
        if (order == null) {
            throw new ServiceException(ErrorCode.NULL_OR_EMPTY_VALUE);
        }

        List<OrderProductResponseDto> productDtos = order.getOrderProducts().stream()
                .map(orderProduct -> OrderProductResponseDto.builder()
                        .productId(orderProduct.getProduct().getId())
                        .quantity(orderProduct.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .orderAddress(order.getOrderAddress())
                .orderRequest(order.getOrderRequest())
                .paymentStatus(order.getPaymentStatus())
                .orderCategory(order.getOrderCategory())
                .userId(order.getUser().getId())
                .storeId(order.getStore().getId())
                .build();
    }
}
