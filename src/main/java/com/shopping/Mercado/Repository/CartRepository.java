package com.shopping.Mercado.Repository;

import com.shopping.Mercado.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
