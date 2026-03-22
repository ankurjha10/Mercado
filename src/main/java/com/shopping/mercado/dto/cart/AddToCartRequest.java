package com.shopping.mercado.dto.cart;

import lombok.Data;

import java.util.UUID;

@Data
public class AddToCartRequest {
    public UUID userId;
    public UUID productId;
    public int quantity;
}
