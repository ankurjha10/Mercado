package com.shopping.Mercado.Dto.ProductDTO;

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
