package com.shinde.desicart.service.impl;

import com.shinde.desicart.model.*;
import com.shinde.desicart.dto.OrderDto;
import com.shinde.desicart.dto.OrderItemDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.repository.*;
import com.shinde.desicart.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    private ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(Long userId, String paymentId) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Cart not found for user with id: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new CustomException("Cannot create order with empty cart");
        }

        // Calculate total amount
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentId(paymentId);
        order.setPaymentStatus("PAID");

        Order savedOrder = orderRepository.save(order);

        // Create order items and update product stock
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Check stock availability
            if (product.getStock() < cartItem.getQuantity()) {
                throw new CustomException("Insufficient stock for product: " + product.getName());
            }

            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }

        // Clear the cart
        cartItemRepository.deleteAll(cart.getItems());

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto getOrderById(Long orderId) throws CustomException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found with id: " + orderId));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrdersByUser(Long userId) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        return orderRepository.findByUser(user).stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String status) throws CustomException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found with id: " + orderId));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) throws CustomException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found with id: " + orderId));

        return order.getItems().stream()
                .map(item -> modelMapper.map(item, OrderItemDto.class))
                .collect(Collectors.toList());
    }
}
