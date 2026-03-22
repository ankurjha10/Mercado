package com.shopping.mercado.dto.category;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryListResponse {
    public UUID categoryId;
    public String categoryName;
    public String categoryDescription;
}
