package com.shopping.Mercado.Dto.ProductDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProductRequest {
    public String productName;
    public String productDescription;
    public int productQuantity;
    public BigDecimal productPrice;
    public String productImage;
    public UUID categoryId;
}

