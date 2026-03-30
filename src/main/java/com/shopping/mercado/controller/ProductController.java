package com.shopping.mercado.controller;

import com.shopping.mercado.dto.product.CreateProductRequest;
import com.shopping.mercado.dto.product.ProductDetailResponse;
import com.shopping.mercado.dto.product.ProductListResponse;
import com.shopping.mercado.dto.product.UpdateProductRequest;
import com.shopping.mercado.entity.UserPrincipal;
import com.shopping.mercado.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductDetailResponse> addProduct(@RequestBody CreateProductRequest product, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        UUID userId = userPrincipal.getUser().getUserId();
        return ResponseEntity.ok(productService.addProduct(product, userId));
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
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        UUID userId = userPrincipal.getUser().getUserId();

        productService.deleteProductById(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/id/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductDetailResponse> updateProductById(@PathVariable UUID id, @RequestBody UpdateProductRequest product, @AuthenticationPrincipal UserPrincipal userPrincipal){
        UUID userId = userPrincipal.getUser().getUserId();
        return ResponseEntity.ok(productService.updateProduct(id, product, userId));
    }
}
