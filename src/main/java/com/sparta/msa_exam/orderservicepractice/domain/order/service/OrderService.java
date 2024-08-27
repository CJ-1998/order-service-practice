package com.sparta.msa_exam.orderservicepractice.domain.order.service;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public Order createOrder(Order order, List<OrderProductRequestDto> products) {
        Order createdOrder = orderRepository.save(order);

        for (OrderProductRequestDto productDto : products) {
            Product product = productRepository.findById(productDto.getProductId())
                    .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

            OrderProduct orderProduct = new OrderProduct(createdOrder, product, productDto.getQuantity());
            orderProductRepository.save(orderProduct);
            createdOrder.addOrderProduct(orderProduct);
        }

        return createdOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findAllByOrderStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByUserId(UUID userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findAllByOrderStatus(status, pageable);
    }

    @Transactional
    public Order updateOrder(UUID orderId, Order orderToUpdate, List<OrderProductRequestDto> products) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.updateDetails(orderToUpdate);
                    orderProductRepository.deleteByOrderId(orderId);

                    for (OrderProductRequestDto productDto : products) {
                        Product product = productRepository.findById(productDto.getProductId())
                                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

                        OrderProduct orderProduct = new OrderProduct(order, product, productDto.getQuantity());
                        orderProductRepository.save(orderProduct);
                        order.addOrderProduct(orderProduct);
                    }

                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public Order cancelOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.cancelOrder();
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public Order deleteOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.deleteOrder();
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public Order addProductToOrder(UUID orderId, UUID productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        OrderProduct orderProduct = new OrderProduct(order, product, quantity);
        orderProductRepository.save(orderProduct);

        order.addOrderProduct(orderProduct);
        return orderRepository.save(order);
    }

    @Transactional
    public Order removeProductFromOrder(UUID orderId, UUID productId) {
        OrderProduct orderProduct = orderProductRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        orderProductRepository.delete(orderProduct);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
        order.removeOrderProduct(orderProduct);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateProductQuantity(UUID orderId, UUID productId, Integer quantity) {
        OrderProduct orderProduct = orderProductRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        orderProduct.updateQuantity(quantity);
        orderProductRepository.save(orderProduct);

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }
}
