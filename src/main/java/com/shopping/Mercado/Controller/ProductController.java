package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Entity.Product;
import com.shopping.Mercado.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all-products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add-product")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }


}
