package com.shopping.Mercado.Service;

import com.shopping.Mercado.Entity.Product;
import com.shopping.Mercado.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}
