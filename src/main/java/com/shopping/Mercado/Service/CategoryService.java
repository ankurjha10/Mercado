package com.shopping.Mercado.Service;

import com.shopping.Mercado.Dto.CategoryDTO.CategoryDetailResponse;
import com.shopping.Mercado.Dto.CategoryDTO.CategoryListResponse;
import com.shopping.Mercado.Dto.CategoryDTO.CreateCategoryRequest;
import com.shopping.Mercado.Entity.Category;
import com.shopping.Mercado.Entity.Product;
import com.shopping.Mercado.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryListResponse toListResponse(Category c) {
        CategoryListResponse res = new CategoryListResponse();
        res.categoryId = c.getCategoryId();
        res.categoryName = c.getCategoryName();
        res.categoryDescription = c.getCategoryDescription();
        return res;
    }

    private CategoryDetailResponse toDetailResponse(Category c) {
        CategoryDetailResponse res = new CategoryDetailResponse();
        res.categoryId = c.getCategoryId();
        res.categoryName = c.getCategoryName();
        res.categoryDescription = c.getCategoryDescription();
        res.products = c.getProducts()
                .stream()
                .map(Product::getProductId)
                .toList();
        return res;
    }


    public CategoryListResponse addCategory(CreateCategoryRequest dto) {
        Category category =  new Category();
        category.setCategoryName(dto.categoryName);
        category.setCategoryDescription(dto.categoryDescription);
        Category saved = categoryRepository.save(category);

        CategoryListResponse res = new CategoryListResponse();
        res.categoryId = saved.getCategoryId();
        res.categoryName = saved.getCategoryName();
        res.categoryDescription = saved.getCategoryDescription();

        return res;
    }

    public CategoryDetailResponse getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .stream()
                .findFirst()
                .map(this::toDetailResponse)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
    }

    public CategoryDetailResponse getCategoryByName(String name) {
        return categoryRepository.findCategoriesByCategoryName(name)
                .stream()
                .findFirst()
                .map(this::toDetailResponse)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with name: " + name));
    }

    public List<CategoryListResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toListResponse).toList();
    }

    public void deleteCategoryById(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    public
}
