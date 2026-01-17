package com.shopping.Mercado.Dto.ProductDTO;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateProductRequest {
    public String productName;
    public String productDescription;
    public int productQuantity;
    public BigDecimal productPrice;
    public String productImage;
    public UUID categoryId;
}

