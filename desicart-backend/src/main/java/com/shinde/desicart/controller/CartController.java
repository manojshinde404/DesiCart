package com.shinde.desicart.controller;

import com.shinde.desicart.dto.CartDto;
import com.shinde.desicart.dto.CartItemDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable Long userId) throws CustomException {
        CartDto cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/items/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable Long userId) throws CustomException {
        List<CartItemDto> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> addToCart(
            @RequestParam Long userId,
            @RequestBody CartItemDto cartItemDto) throws CustomException {
        CartDto cart = cartService.addToCart(userId, cartItemDto);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> updateCartItem(
            @RequestParam Long userId,
            @PathVariable Long itemId,
            @RequestBody CartItemDto cartItemDto) throws CustomException {
        CartDto cart = cartService.updateCartItem(userId, itemId, cartItemDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeFromCart(
            @RequestParam Long userId,
            @PathVariable Long itemId) throws CustomException {
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) throws CustomException {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
