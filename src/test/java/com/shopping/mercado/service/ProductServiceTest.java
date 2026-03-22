package com.shopping.mercado.service;

import com.shopping.mercado.dto.product.CreateProductRequest;
import com.shopping.mercado.dto.product.UpdateProductRequest;
import com.shopping.mercado.entity.Category;
import com.shopping.mercado.entity.Product;
import com.shopping.mercado.repository.CategoryRepository;
import com.shopping.mercado.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product createProduct() {
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        category.setCategoryName("Electronics");
        category.setCategoryDescription("Devices");

        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Phone");
        product.setProductDescription("Nice phone");
        product.setProductPrice(BigDecimal.valueOf(10000));
        product.setProductStock(5);
        product.setProductImage("img.png");
        product.setProductCategory(category);

        return product;
    }

    // ✅ getAllProducts
    @Test
    void shouldReturnAllProducts() {
        Product product = createProduct();
        when(productRepository.findAll()).thenReturn(List.of(product));

        var result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals(product.getProductId(), result.getFirst().productId);
        assertEquals(product.getProductName(), result.getFirst().productName);
        assertEquals(product.getProductPrice(), result.getFirst().productPrice);
        assertEquals(product.getProductImage(), result.getFirst().productImage);
        assertEquals(product.getProductCategory().getCategoryName(), result.getFirst().productCategory);
    }

    // ✅ getProductById success
    @Test
    void shouldReturnProductById() {
        Product product = createProduct();
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));

        var result = productService.getProductById(product.getProductId());

        assertEquals(product.getProductId(), result.productId);
        assertEquals(product.getProductName(), result.productName);
        assertEquals(product.getProductDescription(), result.productDescription);
        assertEquals(product.getProductPrice(), result.productPrice);
        assertEquals(product.getProductImage(), result.productImage);
        assertEquals(product.getProductStock(), result.productQuantity);
        assertEquals(product.getProductCategory().getCategoryName(), result.categoryName);
    }

    // ❌ getProductById fail
    @Test
    void shouldThrowWhenProductNotFoundById() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.getProductById(id));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found with id: " + id, exception.getReason());
    }

    // ✅ getProductByName success
    @Test
    void shouldReturnProductByName() {
        Product product = createProduct();

        when(productRepository.findProductByProductName("Phone"))
                .thenReturn(Optional.of(product));

        var result = productService.getProductByName("Phone");

        assertEquals(product.getProductId(), result.productId);
        assertEquals("Phone", result.productName);
        assertEquals(product.getProductCategory().getCategoryName(), result.categoryName);
    }

    // ❌ getProductByName fail
    @Test
    void shouldThrowWhenProductNotFoundByName() {
        when(productRepository.findProductByProductName("ABC"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.getProductByName("ABC"));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found with name: ABC", exception.getReason());
    }

    // ✅ addProduct success
    @Test
    void shouldAddProduct() {
        Category category = new Category();
        UUID categoryId = UUID.randomUUID();
        category.setCategoryId(categoryId);
        category.setCategoryName("Electronics");
        category.setCategoryDescription("Devices");

        CreateProductRequest dto = new CreateProductRequest();
        dto.setProductName("Laptop");
        dto.setProductDescription("Gaming");
        dto.setProductImage("img.png");
        dto.setProductPrice(BigDecimal.valueOf(50000));
        dto.setProductQuantity(10);
        dto.setCategoryId(categoryId);

        when(categoryRepository.findById(dto.getCategoryId()))
                .thenReturn(Optional.of(category));

        Product saved = new Product();
        saved.setProductId(UUID.randomUUID());
        saved.setProductName(dto.getProductName());
        saved.setProductDescription(dto.getProductDescription());
        saved.setProductImage(dto.getProductImage());
        saved.setProductPrice(dto.getProductPrice());
        saved.setProductStock(dto.getProductQuantity());
        saved.setProductCategory(category);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        var result = productService.addProduct(dto);

        assertEquals(saved.getProductId(), result.productId);
        assertEquals(dto.getProductName(), result.productName);
        assertEquals(dto.getProductDescription(), result.productDescription);
        assertEquals(dto.getProductImage(), result.productImage);
        assertEquals(dto.getProductPrice(), result.productPrice);
        assertEquals(dto.getProductQuantity(), result.productQuantity);
        assertEquals(category.getCategoryName(), result.categoryName);

        verify(productRepository).save(argThat(product ->
                dto.getProductName().equals(product.getProductName())
                        && dto.getProductDescription().equals(product.getProductDescription())
                        && dto.getProductImage().equals(product.getProductImage())
                        && dto.getProductPrice().equals(product.getProductPrice())
                        && dto.getProductQuantity() == product.getProductStock()
                        && category.equals(product.getProductCategory())
        ));
    }

    // ❌ addProduct fail
    @Test
    void shouldThrowWhenCategoryNotFound() {
        CreateProductRequest dto = new CreateProductRequest();
        UUID categoryId = UUID.randomUUID();
        dto.setCategoryId(categoryId);

        when(categoryRepository.findById(dto.getCategoryId()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.addProduct(dto));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found with id: " + categoryId, exception.getReason());
    }

    // ✅ deleteProduct success
    @Test
    void shouldDeleteProduct() {
        Product product = createProduct();
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));

        productService.deleteProductById(product.getProductId());

        verify(productRepository).delete(product);
    }

    // ❌ deleteProduct fail
    @Test
    void shouldThrowWhenDeleteProductNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.deleteProductById(id));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found with id: " + id, exception.getReason());
    }

    // ✅ updateProduct (name change)
    @Test
    void shouldUpdateProductName() {
        Product product = createProduct();
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));

        UpdateProductRequest dto = new UpdateProductRequest();
        dto.productName = "Updated";
        dto.productDescription = "Updated description";
        dto.productImage = "updated.png";
        dto.productPrice = BigDecimal.valueOf(12000);
        dto.productQuantity = 9;

        var result = productService.updateProduct(product.getProductId(), dto);

        assertEquals("Updated", result.productName);
        assertEquals("Updated description", result.productDescription);
        assertEquals("updated.png", result.productImage);
        assertEquals(BigDecimal.valueOf(12000), result.productPrice);
        assertEquals(9, result.productQuantity);
        assertEquals("Electronics", result.categoryName);
    }

    // ✅ updateProduct (category change)
    @Test
    void shouldUpdateCategory() {
        Product product = createProduct();
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));

        Category newCategory = new Category();
        newCategory.setCategoryName("New Category");

        UpdateProductRequest dto = new UpdateProductRequest();
        dto.categoryId = UUID.randomUUID();

        when(categoryRepository.findById(dto.categoryId))
                .thenReturn(Optional.of(newCategory));

        var result = productService.updateProduct(product.getProductId(), dto);

        assertEquals("New Category", result.categoryName);
        assertEquals(newCategory, product.getProductCategory());
    }

    @Test
    void shouldThrowWhenUpdateProductNotFound() {
        UUID productId = UUID.randomUUID();
        UpdateProductRequest dto = new UpdateProductRequest();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.updateProduct(productId, dto));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found with id: " + productId, exception.getReason());
    }

    @Test
    void shouldThrowWhenUpdateCategoryNotFound() {
        Product product = createProduct();
        UUID categoryId = UUID.randomUUID();
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        UpdateProductRequest dto = new UpdateProductRequest();
        dto.categoryId = categoryId;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.updateProduct(product.getProductId(), dto));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found with id: " + categoryId, exception.getReason());
    }
}
