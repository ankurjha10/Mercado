package com.shopping.Mercado.Dto.ProductDTO;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductDetailResponse {
    public UUID productId;
    public String productName;
    public String productDescription;
    public String categoryName;
    public BigDecimal productPrice;
    public int productQuantity;
    public String productImage;
}

