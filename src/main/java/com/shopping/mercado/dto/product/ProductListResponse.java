package com.shopping.mercado.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductListResponse {
    public UUID productId;
    public String productName;
    public BigDecimal productPrice;
    public String productImage;
    public String productCategory;
}
