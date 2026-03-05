package com.shopping.Mercado.Dto.CartDTO;

import lombok.Data;

@Data
public class AddToCartRequest {
    public String userId;
    public String productId;
    public int quantity;
}
