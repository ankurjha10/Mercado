package com.shopping.Mercado.Dto.CartDTO;

import com.shopping.Mercado.Dto.ProductDTO.ProductListResponse;
import lombok.Data;

import java.util.UUID;

@Data
public class CartItemResponse {
    public UUID cartItemId;
    public ProductListResponse product;
    public int quantity;
}

