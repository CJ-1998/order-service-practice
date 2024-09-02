package com.sparta.msa_exam.orderservicepractice.domain.order.service;

import com.sparta.msa_exam.orderservicepractice.domain.order.domain.Order;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.dtos.OrderRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.order.domain.enums.OrderStatus;
import com.sparta.msa_exam.orderservicepractice.domain.order.repository.OrderRepository;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.domain.OrderProduct;
import com.sparta.msa_exam.orderservicepractice.domain.order_product.service.OrderProductService;
import com.sparta.msa_exam.orderservicepractice.domain.payment.domain.enums.PaymentStatus;
import com.sparta.msa_exam.orderservicepractice.domain.product.domain.Product;
import com.sparta.msa_exam.orderservicepractice.domain.product.repository.ProductRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.StoreRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final OrderProductService orderProductService;

    @Transactional
    public Order createOrder(OrderRequestDto orderDto, UserDetailsImpl userDetails) {
        User user = getUserById(userDetails.getUser().getId());
        Store store = getStoreById(orderDto.getStoreId());

        Order order = Order.builder()
                .orderAddress(orderDto.getOrderAddress())
                .orderRequest(orderDto.getOrderRequest())
                .orderCategory(orderDto.getOrderCategory())
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .user(user)
                .store(store)
                .build();

        order = orderRepository.save(order);

        if (orderDto.hasOrderProducts()) {
            orderProductService.createAndAddOrderProducts(order, orderDto.getOrderProducts());
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByUserId(Long userId, Pageable pageable) {
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

    public Order updateOrder(UUID orderId, OrderRequestDto orderRequestDto) {
        Order order = getOrderById(orderId);

        order.updateOrderAddress(orderRequestDto.getOrderAddress());
        order.updateOrderRequest(orderRequestDto.getOrderRequest());
        order.updateOrderCategory(orderRequestDto.getOrderCategory());

        if (orderRequestDto.hasOrderProducts()) {
            List<OrderProduct> newOrderProducts = orderRequestDto.getOrderProducts().stream()
                    .map(dto -> {
                        Product product = productRepository.findById(dto.getProductId())
                                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
                        return new OrderProduct(order, product, dto.getQuantity());
                    })
                    .collect(Collectors.toList());

            order.updateOrderProducts(newOrderProducts);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.cancelOrder();
        return orderRepository.save(order);
    }

    @Transactional
    public Order deleteOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.deleteOrder();
        return orderRepository.save(order);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    private Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }
}
