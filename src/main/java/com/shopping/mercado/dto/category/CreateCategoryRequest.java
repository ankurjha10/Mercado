package com.shopping.mercado.dto.category;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    public String categoryName;
    public String categoryDescription;
}
