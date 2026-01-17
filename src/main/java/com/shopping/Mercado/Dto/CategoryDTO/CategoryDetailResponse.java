package com.shopping.Mercado.Dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public class CategoryDetailResponse {
    public UUID categoryId;
    public String categoryName;
    public String categoryDescription;
    public List<UUID> products;
}
