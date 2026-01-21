package com.shopping.Mercado.Dto.CategoryDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoryDetailResponse {
    public UUID categoryId;
    public String categoryName;
    public String categoryDescription;
    public List<UUID> products;
}
