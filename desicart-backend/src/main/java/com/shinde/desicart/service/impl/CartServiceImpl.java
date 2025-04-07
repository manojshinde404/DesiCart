package com.shinde.desicart.service.impl;

import com.shinde.desicart.model.*;
import com.shinde.desicart.dto.CartDto;
import com.shinde.desicart.dto.CartItemDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.repository.CartItemRepository;
import com.shinde.desicart.repository.CartRepository;
import com.shinde.desicart.repository.ProductRepository;
import com.shinde.desicart.repository.UserRepository;
import com.shinde.desicart.service.CartService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartServiceImpl implements CartService {

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
    public CartDto getCartByUser(Long userId) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto addToCart(Long userId, CartItemDto cartItemDto) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new CustomException("Product not found with id: " + cartItemDto.getProductId()));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemDto.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(newItem);
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto updateCartItem(Long userId, Long itemId, CartItemDto cartItemDto) throws CustomException {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException("Cart item not found with id: " + itemId));

        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new CustomException("You are not authorized to update this cart item");
        }

        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItem);

        return getCartByUser(userId);
    }

    @Override
    public void removeFromCart(Long userId, Long itemId) throws CustomException {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException("Cart item not found with id: " + itemId));

        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new CustomException("You are not authorized to remove this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Cart not found for user with id: " + userId));

        cartItemRepository.deleteAll(cart.getItems());
    }

    @Override
    public List<CartItemDto> getCartItems(Long userId) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Cart not found for user with id: " + userId));

        return cart.getItems().stream()
                .map(item -> modelMapper.map(item, CartItemDto.class))
                .collect(Collectors.toList());
    }
}
