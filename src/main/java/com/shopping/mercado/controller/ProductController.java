package com.shopping.mercado.controller;

import com.shopping.mercado.dto.product.CreateProductRequest;
import com.shopping.mercado.dto.product.ProductDetailResponse;
import com.shopping.mercado.dto.product.ProductListResponse;
import com.shopping.mercado.dto.product.UpdateProductRequest;
import com.shopping.mercado.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductListResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add-product")
    public ProductDetailResponse addProduct(@RequestBody CreateProductRequest product) {
        return productService.addProduct(product);
    }

    @GetMapping("/name/{name}")
    public ProductDetailResponse getProductByName(@PathVariable String name){
        return productService.getProductByName(name);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<ProductDetailResponse> updateProductById(@PathVariable UUID id, @RequestBody UpdateProductRequest product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }
}
