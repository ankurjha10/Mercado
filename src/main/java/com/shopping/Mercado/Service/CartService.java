package com.shopping.Mercado.Service;

import com.shopping.Mercado.Dto.CartDTO.AddToCartRequest;
import com.shopping.Mercado.Dto.CartDTO.CartItemResponse;
import com.shopping.Mercado.Dto.CartDTO.CartResponse;
import com.shopping.Mercado.Dto.ProductDTO.ProductListResponse;
import com.shopping.Mercado.Entity.Cart;
import com.shopping.Mercado.Entity.CartItem;
import com.shopping.Mercado.Entity.Product;
import com.shopping.Mercado.Entity.User;
import com.shopping.Mercado.Repository.CartItemRepository;
import com.shopping.Mercado.Repository.CartRepository;
import com.shopping.Mercado.Repository.ProductRepository;
import com.shopping.Mercado.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartResponse getCart(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        User user = userRepository.findById(UUID.fromString(request.userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Product product = productRepository.findById(UUID.fromString(request.productId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Find or create cart
        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    // Don't set cartId - let database generate it
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
        response.setUserId(cart.getUser().getUserId());
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

