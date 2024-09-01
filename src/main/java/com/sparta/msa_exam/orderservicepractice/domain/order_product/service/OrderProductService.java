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
        Product product = getProductById(productRequest.getProductId());

        OrderProduct orderProduct = new OrderProduct(order, product, productRequest.getQuantity());
        order.addOrderProduct(orderProduct);

        orderProductRepository.save(orderProduct);
        order.updateTotalPrice();

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateProductQuantity(UUID orderId, UUID productId, Integer quantity) {

        OrderProduct orderProduct = getOrderProductByOrderIdAndProductId(orderId, productId);
        Order order = orderProduct.getOrder();

        orderProduct.updateQuantity(quantity);
        order.updateTotalPrice();

        return orderRepository.save(order);
    }

    @Transactional
    public OrderProduct removeProductFromOrder(UUID orderId, UUID productId) {
        OrderProduct orderProduct = orderProductRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        orderProductRepository.delete(orderProduct);

        return orderProduct;
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    private Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    private OrderProduct getOrderProductByOrderIdAndProductId(UUID orderId, UUID productId) {
        return orderProductRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    public List<OrderProduct> mapToOrderProducts(Order order, List<OrderProductRequestDto> productRequestDtos) {
        return productRequestDtos.stream()
                .map(dto -> new OrderProduct(order, getProductById(dto.getProductId()), dto.getQuantity()))
                .collect(Collectors.toList());
    }
}
