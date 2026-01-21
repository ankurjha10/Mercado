package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Dto.CategoryDTO.CategoryDetailResponse;
import com.shopping.Mercado.Dto.CategoryDTO.CategoryListResponse;
import com.shopping.Mercado.Dto.CategoryDTO.CreateCategoryRequest;
import com.shopping.Mercado.Dto.CategoryDTO.UpdateCategoryRequest;
import com.shopping.Mercado.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

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
