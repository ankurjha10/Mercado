package com.shopping.mercado.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopping.mercado.dto.cart.AddToCartRequest;
import com.shopping.mercado.dto.cart.CartItemResponse;
import com.shopping.mercado.dto.cart.CartResponse;
import com.shopping.mercado.dto.product.ProductListResponse;
import com.shopping.mercado.entity.Cart;
import com.shopping.mercado.entity.CartItem;
import com.shopping.mercado.entity.Product;
import com.shopping.mercado.entity.User;
import com.shopping.mercado.repository.CartItemRepository;
import com.shopping.mercado.repository.CartRepository;
import com.shopping.mercado.repository.ProductRepository;
import com.shopping.mercado.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartResponse getCart(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        User user = userRepository.findById(request.userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Product product = productRepository.findById(request.productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Find or create cart
        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(user);
                    return cartRepository.save(newCart);
                });

        // Check if product already in cart
        boolean productExists = cart.getCartItems().stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(product.getProductId()));

        if (productExists) {
            // Update quantity if product already exists
            cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                    .forEach(item -> item.setQuantity(item.getQuantity() + request.quantity));
        } else {
            // Add new item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.quantity);
            cart.getCartItems().add(cartItem);
        }

        // Save cart with items
        cart = cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse removeFromCart(String userId, String cartItemId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        CartItem cartItem = cartItemRepository.findById(UUID.fromString(cartItemId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));

        cartItemRepository.delete(cartItem);

        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse clearCart(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        cart.getCartItems().clear();
        Cart updatedCart = cartRepository.save(cart);

        return mapToCartResponse(updatedCart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setUserId(cart.getCustomer().getUserId());
        if (cart.getCartItems() != null) {
            response.setCartItems(cart.getCartItems().stream()
                    .map(this::mapToCartItemResponse)
                    .toList());
        } else {
            response.setCartItems(new java.util.ArrayList<>());
        }
        return response;
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(cartItem.getCartItemId());
        response.setQuantity(cartItem.getQuantity());

        ProductListResponse productResponse = new ProductListResponse();
        Product product = cartItem.getProduct();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductPrice(product.getProductPrice());
        productResponse.setProductImage(product.getProductImage());
        productResponse.setProductCategory(product.getProductCategory().getCategoryName());

        response.setProduct(productResponse);
        return response;
    }
}

