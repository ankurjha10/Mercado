package com.shopping.Mercado.Repository;

import com.shopping.Mercado.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findProductByProductName(String productName);
}
