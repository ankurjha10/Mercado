package com.shopping.Mercado.Dto.CartDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    public UUID cartId;
    public UUID userId;
    public List<CartItemResponse> cartItems;
}

