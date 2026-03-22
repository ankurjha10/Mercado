package com.shopping.mercado.dto.cart;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    public UUID cartId;
    public UUID userId;
    public List<CartItemResponse> cartItems;
}

