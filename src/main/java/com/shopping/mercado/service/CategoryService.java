package com.shopping.mercado.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopping.mercado.dto.category.CategoryDetailResponse;
import com.shopping.mercado.dto.category.CategoryListResponse;
import com.shopping.mercado.dto.category.CreateCategoryRequest;
import com.shopping.mercado.dto.category.UpdateCategoryRequest;
import com.shopping.mercado.entity.Category;
import com.shopping.mercado.entity.Product;
import com.shopping.mercado.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

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

    @Transactional
    public void deleteCategoryById(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryListResponse updateCategory(UUID id, UpdateCategoryRequest dto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));

        if (dto.categoryName != null)
            category.setCategoryName(dto.categoryName);

        if (dto.categoryDescription != null)
            category.setCategoryDescription(dto.categoryDescription);

        categoryRepository.save(category);
        return toListResponse(category);
    }
}
