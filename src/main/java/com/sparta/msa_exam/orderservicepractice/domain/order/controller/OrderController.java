package com.sparta.msa_exam.orderservicepractice.domain.order.controller;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.mapper.OrderMapper;
import com.sparta.msa_exam.orderservicepractice.domain.order.service.OrderService;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.mapper.OrderProductMapper;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;

    @PostMapping // 주문 생성
    public ResponseEntity<ResponseBody<OrderResponseDto>> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Order createdOrder = orderService.createOrder(orderRequestDto, userDetails);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(createdOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @GetMapping("/{orderId}") // 단건 주문 조회
    public ResponseEntity<ResponseBody<OrderResponseDto>> getOrderById(@PathVariable UUID orderId) {
        Order foundOrder = orderService.getOrderById(orderId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(foundOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @GetMapping // 전체 주문 조회
    public ResponseEntity<ResponseBody<Page<OrderResponseDto>>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = createPageRequest(page, size, sortBy, sortDirection);
        Page<Order> orderPage = status != null
                ? orderService.getOrdersByStatus(status, pageable)
                : orderService.getAllOrders(pageable);

        Page<OrderResponseDto> responseDtos = orderPage.map(orderMapper::toOrderResponseDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @GetMapping("/user/{userId}") // 특정 유저의 주문 조회
    public ResponseEntity<ResponseBody<Page<OrderResponseDto>>> getOrdersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = createPageRequest(page, size, sortBy, sortDirection);
        Page<Order> orderPage = orderService.getOrdersByUserId(userId, pageable);

        Page<OrderResponseDto> responseDtos = orderPage.map(orderMapper::toOrderResponseDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @PutMapping("/{orderId}") // 주문 수정
    public ResponseEntity<ResponseBody<OrderResponseDto>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {

        Order updatedOrder = orderService.updateOrder(orderId, orderRequestDto);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(updatedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PatchMapping("/{orderId}/cancel") // 주문 취소
    public ResponseEntity<ResponseBody<OrderResponseDto>> cancelOrder(@PathVariable UUID orderId) {
        Order canceledOrder = orderService.cancelOrder(orderId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(canceledOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Secured({UserRole.Authority.ADMIN})
    @DeleteMapping("/{orderId}") // 주문 삭제
    public ResponseEntity<ResponseBody<OrderResponseDto>> deleteOrder(@PathVariable UUID orderId) {
        Order deletedOrder = orderService.deleteOrder(orderId);
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(deletedOrder);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    private Pageable createPageRequest(int page, int size, String sortBy, String sortDirection) {
        // 페이지 크기 제한: 10, 30, 50 이외의 값은 10으로 고정
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        // 정렬 방향 처리
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
