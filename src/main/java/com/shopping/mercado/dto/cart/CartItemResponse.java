package com.shopping.mercado.dto.cart;

import com.shopping.mercado.dto.product.ProductListResponse;
import lombok.Data;

import java.util.UUID;

@Data
public class CartItemResponse {
    public UUID cartItemId;
    public ProductListResponse product;
    public int quantity;
}

