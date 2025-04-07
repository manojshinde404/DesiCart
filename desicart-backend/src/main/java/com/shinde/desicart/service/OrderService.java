package com.shinde.desicart.service;

import com.shinde.desicart.dto.OrderDto;
import com.shinde.desicart.dto.OrderItemDto;
import com.shinde.desicart.exception.CustomException;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, String paymentId) throws CustomException;
    OrderDto getOrderById(Long orderId) throws CustomException;
    List<OrderDto> getOrdersByUser(Long userId) throws CustomException;
    OrderDto updateOrderStatus(Long orderId, String status) throws CustomException;
    List<OrderItemDto> getOrderItems(Long orderId) throws CustomException;
}
