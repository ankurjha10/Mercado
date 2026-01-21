package com.shopping.Mercado.Dto.ProductDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductDetailResponse {
    public UUID productId;
    public String productName;
    public String productDescription;
    public String categoryName;
    public BigDecimal productPrice;
    public int productQuantity;
    public String productImage;
}

