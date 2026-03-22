package com.shopping.mercado.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shopping.mercado.dto.category.CategoryDetailResponse;
import com.shopping.mercado.dto.category.CategoryListResponse;
import com.shopping.mercado.dto.category.CreateCategoryRequest;
import com.shopping.mercado.dto.category.UpdateCategoryRequest;
import com.shopping.mercado.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all-categories")
    public ResponseEntity<List<CategoryListResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDetailResponse> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryDetailResponse> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryListResponse> addCategory(@RequestBody CreateCategoryRequest category){
        return ResponseEntity.ok(categoryService.addCategory(category));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable UUID id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<CategoryListResponse> updateCategoryById(@PathVariable UUID id, @RequestBody UpdateCategoryRequest category){
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }
}
