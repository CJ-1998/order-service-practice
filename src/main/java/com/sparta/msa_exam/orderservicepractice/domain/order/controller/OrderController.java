package com.sparta.msa_exam.orderservicepractice.domain.order.controller;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        List<OrderProductRequestDto> products = orderRequestDto.getProducts();
        Order createdOrder = orderService.createOrder(orderRequestDto, products);

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
    public ResponseEntity<ResponseBody<Page<OrderResponseDto>>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        // 페이지 크기 제한: 10, 30, 50 이외의 값은 10으로 고정
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        // 정렬 방향 처리
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Order> orderPage;

        if (status != null) {
            orderPage = orderService.getOrdersByStatus(status, pageable);
        } else {
            orderPage = orderService.getAllOrders(pageable);
        }

        Page<OrderResponseDto> responseDtos = orderPage.map(orderMapper::toOrderResponseDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @GetMapping("/{userId}") // 특정 유저의 주문 조회
    public ResponseEntity<ResponseBody<Page<OrderResponseDto>>> getOrdersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        // 페이지 크기 제한: 10, 30, 50 이외의 값은 10으로 고정
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        // 정렬 방향 처리
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Order> orderPage = orderService.getOrdersByUserId(userId, pageable);

        Page<OrderResponseDto> responseDtos = orderPage.map(orderMapper::toOrderResponseDto);

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
