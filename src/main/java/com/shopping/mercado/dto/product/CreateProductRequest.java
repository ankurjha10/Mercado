package com.shopping.mercado.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProductRequest {
    private String productName;
    private String productDescription;
    private int productStock;
    private BigDecimal productPrice;
    private String productImage;
    private UUID categoryId;
}

