package com.sparta.msa_exam.orderservicepractice.domain.order.domain.controller;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.mapper.OrderMapper;
import com.sparta.msa_exam.orderservicepractice.domain.order.service.OrderService;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping // 주문 생성
    public ResponseEntity<ResponseBody<OrderResponseDto>> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto) {

        Order order = orderMapper.toOrder(orderRequestDto);
        List<OrderProductRequestDto> products = orderRequestDto.getProducts();
        Order createdOrder = orderService.createOrder(order, products);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(createdOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @GetMapping("/{orderId}") // 단건 주문 조회
    public ResponseEntity<ResponseBody<OrderResponseDto>> getOrderById(@PathVariable UUID orderId) {
        Order foundOrder = orderService.getOrderById(orderId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(foundOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @GetMapping // 전체 주문 조회
    public ResponseEntity<ResponseBody<List<OrderResponseDto>>> getAllOrders(@RequestParam(required = false) OrderStatus status) {

        List<Order> orders;
        if (status != null) {
            orders = orderService.getOrdersByStatus(status);
        } else {
            orders = orderService.getAllOrders();
        }

        List<OrderResponseDto> responseDtos = orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @GetMapping("/{userId}") // 특정 유저의 주문 조회
    public ResponseEntity<ResponseBody<List<OrderResponseDto>>> getOrdersByUserId(@PathVariable UUID userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponseDto> responseDtos = orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @PutMapping("/{orderId}") // 주문 수정
    public ResponseEntity<ResponseBody<OrderResponseDto>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {

        Order orderToUpdate = orderMapper.toOrder(orderRequestDto);
        List<OrderProductRequestDto> products = orderRequestDto.getProducts();
        Order updatedOrder = orderService.updateOrder(orderId, orderToUpdate, products);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(updatedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PatchMapping("/{orderId}/cancel") // 주문 취소
    public ResponseEntity<ResponseBody<OrderResponseDto>> cancelOrder(@PathVariable UUID orderId) {
        Order canceledOrder = orderService.cancelOrder(orderId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(canceledOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @DeleteMapping("/{orderId}") // 주문 삭제
    public ResponseEntity<ResponseBody<OrderResponseDto>> deleteOrder(@PathVariable UUID orderId) {
        Order deletedOrder = orderService.deleteOrder(orderId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(deletedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PostMapping("/{orderId}/products/{productId}") // 주문에 제품 추가
    public ResponseEntity<ResponseBody<OrderResponseDto>> addProductToOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestParam Integer quantity) {

        Order updatedOrder = orderService.addProductToOrder(orderId, productId, quantity);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(updatedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PatchMapping("/{orderId}/products/{productId}") // 주문 제품 수량 수정
    public ResponseEntity<ResponseBody<OrderResponseDto>> updateProductQuantity(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestParam Integer quantity) {

        Order updatedOrder = orderService.updateProductQuantity(orderId, productId, quantity);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(updatedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @DeleteMapping("/{orderId}/products/{productId}") // 주문에서 제품 삭제
    public ResponseEntity<ResponseBody<OrderResponseDto>> removeProductFromOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID productId) {

        Order updatedOrder = orderService.removeProductFromOrder(orderId, productId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(updatedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }
}
