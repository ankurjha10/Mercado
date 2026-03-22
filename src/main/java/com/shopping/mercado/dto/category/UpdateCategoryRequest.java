package com.shopping.mercado.dto.category;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    public String categoryName;
    public String categoryDescription;
}
