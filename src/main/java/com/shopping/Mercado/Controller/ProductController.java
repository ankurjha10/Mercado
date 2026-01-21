package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Dto.ProductDTO.CreateProductRequest;
import com.shopping.Mercado.Dto.ProductDTO.ProductDetailResponse;
import com.shopping.Mercado.Dto.ProductDTO.ProductListResponse;
import com.shopping.Mercado.Dto.ProductDTO.UpdateProductRequest;
import com.shopping.Mercado.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

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
