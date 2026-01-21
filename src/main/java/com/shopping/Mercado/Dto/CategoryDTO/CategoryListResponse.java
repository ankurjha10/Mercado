package com.shopping.Mercado.Dto.CategoryDTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryListResponse {
    public UUID categoryId;
    public String categoryName;
    public String categoryDescription;
}
