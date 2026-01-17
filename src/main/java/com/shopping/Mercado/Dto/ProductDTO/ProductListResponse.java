package com.shopping.Mercado.Dto.ProductDTO;

import com.shopping.Mercado.Entity.Category;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductListResponse {
    public UUID productId;
    public String productName;
    public BigDecimal productPrice;
    public String productImage;
    public String productCategory;
}
