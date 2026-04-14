package com.shopping.mercado.service;

import com.shopping.mercado.entity.Seller;
import com.shopping.mercado.repository.CartItemRepository;
import com.shopping.mercado.repository.SellerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopping.mercado.dto.product.CreateProductRequest;
import com.shopping.mercado.dto.product.ProductDetailResponse;
import com.shopping.mercado.dto.product.ProductListResponse;
import com.shopping.mercado.dto.product.UpdateProductRequest;
import com.shopping.mercado.entity.Category;
import com.shopping.mercado.entity.Product;
import com.shopping.mercado.repository.CategoryRepository;
import com.shopping.mercado.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final CartItemRepository cartItemRepository;

    private static final String PRODUCT_NOT_FOUND = "Product not found with id: ";

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
        res.productStock = p.getProductStock();

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND + id));
    }

    public ProductDetailResponse getProductByName(String name){
        return productRepository.findProductByProductName(name)
                .stream()
                .findFirst()
                .map(this::toDetailResponse)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with name: " + name));
    }

    @Transactional
    public ProductDetailResponse addProduct(@NotNull CreateProductRequest dto, UUID userId) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + dto.getCategoryId()));

        Seller seller = sellerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Seller not found"));

        if (!seller.isVerified()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Your profile is not approved yet. Wait for approval");
        }

        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setProductDescription(dto.getProductDescription());
        product.setProductImage(dto.getProductImage());
        product.setProductPrice(dto.getProductPrice());
        product.setProductStock(dto.getProductStock());
        product.setProductCategory(category);
        product.setSeller(seller);

        Product saved = productRepository.save(product);

        ProductDetailResponse res = new ProductDetailResponse();
        res.productId = saved.getProductId();
        res.productName = saved.getProductName();
        res.productDescription = saved.getProductDescription();
        res.productImage = saved.getProductImage();
        res.productStock = saved.getProductStock();
        res.productPrice = saved.getProductPrice();
        res.categoryName = category.getCategoryName();
        return res;
    }

    @Transactional
    public void deleteProductById(UUID id, UUID sellerId) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND + id));

        if (!product.getSeller().getUser().getUserId().equals(sellerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seller not found");
        }

        cartItemRepository.deleteByProduct_ProductId(id);

        productRepository.delete(product);
    }

    @Transactional
    public ProductDetailResponse updateProduct(UUID id, UpdateProductRequest dto, UUID userId) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND + id));

        if (!product.getSeller().getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This Product doesn't belong to you");
        }

        if (dto.productName != null)
            product.setProductName(dto.productName);

        if (dto.productDescription != null)
            product.setProductDescription(dto.productDescription);

        if (dto.productImage != null)
            product.setProductImage(dto.productImage);

        if (dto.productStock != null)
            product.setProductStock(dto.productStock);

        if (dto.productPrice != null)
            product.setProductPrice(dto.productPrice);

        if (dto.categoryId != null) {
            Category category = categoryRepository.findById(dto.categoryId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Category not found with id: " + dto.categoryId));
            product.setProductCategory(category);
        }

        return toDetailResponse(product);
    }
}