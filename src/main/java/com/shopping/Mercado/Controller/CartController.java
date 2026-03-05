package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Dto.CartDTO.AddToCartRequest;
import com.shopping.Mercado.Dto.CartDTO.CartResponse;
import com.shopping.Mercado.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @DeleteMapping("/remove/{userId}/{cartItemId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable String userId, @PathVariable String cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, cartItemId));
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<CartResponse> clearCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}

