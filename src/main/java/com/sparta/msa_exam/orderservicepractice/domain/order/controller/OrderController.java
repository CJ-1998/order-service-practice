package com.sparta.msa_exam.orderservicepractice.domain.order.controller;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.mapper.OrderMapper;
import com.sparta.msa_exam.orderservicepractice.domain.order.service.OrderService;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.service.OrderProductService;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.PagedResponseDto;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ResponseBody<OrderResponseDto>> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Order createdOrder = orderService.createOrder(orderRequestDto, userDetails);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(createdOrder)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseBody<OrderResponseDto>> getOrderById(@PathVariable UUID orderId) {
        Order foundOrder = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(foundOrder)));
    }

    @Secured({UserRole.Authority.ADMIN, UserRole.Authority.OWNER})
    @GetMapping
    public ResponseEntity<ResponseBody<PagedResponseDto<OrderResponseDto>>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Order> orderPage = (status != null)
                ? orderService.getOrdersByStatus(status, pageable)
                : orderService.getAllOrders(pageable);

        Page<OrderResponseDto> responseDtoPage = orderPage.map(orderMapper::toOrderResponseDto);
        PagedResponseDto<OrderResponseDto> pagedResponseDto = new PagedResponseDto<>(responseDtoPage);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(pagedResponseDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseBody<PagedResponseDto<OrderResponseDto>>> getOrdersByUserId(
            @PathVariable Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Order> orderPage = orderService.getOrdersByUserId(userId, pageable);
        Page<OrderResponseDto> responseDtoPage = orderPage.map(orderMapper::toOrderResponseDto);
        PagedResponseDto<OrderResponseDto> pagedResponseDto = new PagedResponseDto<>(responseDtoPage);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(pagedResponseDto));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseBody<OrderResponseDto>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {
        Order updatedOrder = orderService.updateOrder(orderId, orderRequestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(updatedOrder)));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseBody<OrderResponseDto>> cancelOrder(@PathVariable UUID orderId) {
        Order canceledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(canceledOrder)));
    }

    @Secured({UserRole.Authority.ADMIN})
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseBody<OrderResponseDto>> deleteOrder(@PathVariable UUID orderId) {
        Order deletedOrder = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(deletedOrder)));
    }

    @PostMapping("/{orderId}/products")
    public ResponseEntity<ResponseBody<OrderResponseDto>> addProductToOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderProductRequestDto productRequest) {
        Order updatedOrder = orderProductService.addProductToOrder(orderId, productRequest);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(updatedOrder)));
    }

    @PatchMapping("/{orderId}/products/{productId}")
    public ResponseEntity<ResponseBody<OrderResponseDto>> updateProductQuantity(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestBody @Valid OrderProductRequestDto productRequest) {
        Order updatedOrder = orderProductService.updateProductQuantity(orderId, productId, productRequest.getQuantity());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(updatedOrder)));
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<ResponseBody<OrderResponseDto>> removeProductFromOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID productId) {
        Order updatedOrder = orderProductService.removeProductFromOrder(orderId, productId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(orderMapper.toOrderResponseDto(updatedOrder)));
    }
}
