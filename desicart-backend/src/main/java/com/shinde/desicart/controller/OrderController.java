package com.shinde.desicart.controller;

import com.shinde.desicart.dto.OrderDto;
import com.shinde.desicart.dto.OrderItemDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> createOrder(
            @RequestParam Long userId,
            @RequestParam String paymentId) throws CustomException {
        OrderDto order = orderService.createOrder(userId, paymentId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) throws CustomException {
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Long userId) throws CustomException {
        List<OrderDto> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/items/{orderId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Long orderId) throws CustomException {
        List<OrderItemDto> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/update-status/{orderId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) throws CustomException {
        OrderDto order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
}
