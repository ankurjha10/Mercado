package com.shopping.Mercado.Service;

import com.shopping.Mercado.Dto.ProductDTO.CreateProductRequest;
import com.shopping.Mercado.Dto.ProductDTO.ProductDetailResponse;
import com.shopping.Mercado.Dto.ProductDTO.ProductListResponse;
import com.shopping.Mercado.Entity.Category;
import com.shopping.Mercado.Entity.Product;
import com.shopping.Mercado.Repository.CategoryRepository;
import com.shopping.Mercado.Repository.ProductRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductListResponse toListResponse(Product p) {
        ProductListResponse res = new ProductListResponse();
        res.productId = p.getProductId();
        res.productName = p.getProductName();
        res.productPrice = p.getProductPrice();
        res.productImage = p.getProductImage();
        res.productCategory = p.getProductCategory().getCategoryName();

        return res;
    }

    private ProductDetailResponse toDetailResponse(Product p) {
        ProductDetailResponse res = new ProductDetailResponse();
        res.productId = p.getProductId();
        res.productName = p.getProductName();
        res.productDescription = p.getProductDescription();
        res.productPrice = p.getProductPrice();
        res.productImage = p.getProductImage();
        res.categoryName = p.getProductCategory().getCategoryName();
        res.productQuantity = p.getProductQuantity();

        return res;
    }

    public List<ProductListResponse> getAllProducts(){
        return productRepository.findAll().stream().map(this::toListResponse).toList();
    }

    public ProductDetailResponse getProductById(UUID id){
        return productRepository.findById(id)
                .stream()
                .findFirst()
                .map(this::toDetailResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));
    }

    public ProductDetailResponse getProductByName(String name){
        return productRepository.findProductByProductName(name)
                .stream()
                .findFirst()
                .map(this::toDetailResponse)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with name: " + name));
    }

    public ProductDetailResponse addProduct(@NotNull CreateProductRequest dto) {
        Category category = categoryRepository.findById(dto.categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + dto.categoryId));

        Product product = new Product();
        product.setProductName(dto.productName);
        product.setProductDescription(dto.productDescription);
        product.setProductImage(dto.productImage);
        product.setProductPrice(dto.productPrice);
        product.setProductQuantity(dto.productQuantity);
        product.setProductCategory(category);

        Product saved = productRepository.save(product);

        ProductDetailResponse res = new ProductDetailResponse();
        res.productId = saved.getProductId();
        res.productName = saved.getProductName();
        res.productDescription = saved.getProductDescription();
        res.productImage = saved.getProductImage();
        res.productQuantity = saved.getProductQuantity();
        res.productPrice = saved.getProductPrice();
        res.categoryName = category.getCategoryName();
        return res;
    }

    public void deleteProductById(UUID id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));
        productRepository.delete(product);
    }
}