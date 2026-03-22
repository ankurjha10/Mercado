package com.shopping.mercado.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateProductRequest {
    public String productName;
    public String productDescription;
    public BigDecimal productPrice;
    public UUID categoryId;
    public String productImage;
    public Integer productQuantity;
}
