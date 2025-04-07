package com.shinde.desicart.service;

import com.shinde.desicart.dto.CartDto;
import com.shinde.desicart.dto.CartItemDto;
import com.shinde.desicart.exception.CustomException;

import java.util.List;

public interface CartService {
    CartDto getCartByUser(Long userId) throws CustomException;
    CartDto addToCart(Long userId, CartItemDto cartItemDto) throws CustomException;
    CartDto updateCartItem(Long userId, Long itemId, CartItemDto cartItemDto) throws CustomException;
    void removeFromCart(Long userId, Long itemId) throws CustomException;
    void clearCart(Long userId) throws CustomException;
    List<CartItemDto> getCartItems(Long userId) throws CustomException;
}
