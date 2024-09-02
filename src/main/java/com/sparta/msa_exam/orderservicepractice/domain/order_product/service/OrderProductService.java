package com.sparta.msa_exam.orderservicepractice.domain.order_product.service;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.repository.OrderRepository;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.dtos.OrderProductRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.repository.OrderProductRepository;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.repository.ProductRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public Order addProductToOrder(UUID orderId, OrderProductRequestDto productRequest) {
        Order order = getOrderById(orderId);
        OrderProduct newOrderProduct = createOrderProduct(order, productRequest);
        order.addOrderProduct(newOrderProduct);
        return orderRepository.save(order);
    }

    @Transactional
    public void createAndAddOrderProducts(Order order, List<OrderProductRequestDto> productRequests) {
        List<OrderProduct> newOrderProducts = productRequests.stream()
                .map(req -> createOrderProduct(order, req))
                .collect(Collectors.toList());
        order.updateOrderProducts(newOrderProducts);
    }

    @Transactional
    public void updateOrderProducts(Order order, List<OrderProductRequestDto> productRequests) {
        List<OrderProduct> updatedOrderProducts = productRequests.stream()
                .map(req -> createOrderProduct(order, req))
                .collect(Collectors.toList());
        order.updateOrderProducts(updatedOrderProducts);
    }

    @Transactional
    public Order updateProductQuantity(UUID orderId, UUID productId, Integer quantity) {
        Order order = getOrderById(orderId);
        OrderProduct orderProduct = order.getOrderProducts().stream()
                .filter(op -> op.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        orderProduct.updateQuantity(quantity);
        order.updateTotalPrice();
        return orderRepository.save(order);
    }

    @Transactional
    public Order removeProductFromOrder(UUID orderId, UUID productId) {
        Order order = getOrderById(orderId);

        OrderProduct orderProduct = orderProductRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        order.removeOrderProduct(orderProduct);

        return orderRepository.save(order);
    }

    private OrderProduct createOrderProduct(Order order, OrderProductRequestDto request) {
        Product product = getProductById(request.getProductId());
        return new OrderProduct(order, product, request.getQuantity());
    }

    private Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }
}
