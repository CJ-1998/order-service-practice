package com.sparta.msa_exam.orderservicepractice.domain.order_product.controller;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.mapper.OrderProductMapper;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.service.OrderProductService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order-products")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;
    private final OrderProductMapper orderProductMapper;

    @PostMapping("/{orderId}/products")
    public ResponseEntity<ResponseBody<OrderProductResponseDto>> addProductToOrder(
            @PathVariable UUID orderId,
            @RequestBody OrderProductRequestDto productRequest) {

        Order updatedOrder = orderProductService.addProductToOrder(orderId, productRequest);
        OrderProductResponseDto responseDto = orderProductMapper.toOrderProductResponseDto(updatedOrder, productRequest);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PatchMapping("/{orderId}/products/{productId}")
    public ResponseEntity<ResponseBody<OrderProductResponseDto>> updateProductQuantity(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestBody OrderProductRequestDto productRequest) {

        Order updatedOrder = orderProductService.updateProductQuantity(orderId, productId, productRequest.getQuantity());
        OrderProductResponseDto responseDto = orderProductMapper.toOrderProductResponseDto(updatedOrder, productRequest);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<ResponseBody<OrderProductResponseDto>> removeProductFromOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID productId) {

        OrderProduct updatedOrderProduct = orderProductService.removeProductFromOrder(orderId, productId);
        OrderProductResponseDto responseDto = orderProductMapper.toOrderProductResponseDto(updatedOrderProduct);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }
}
