package com.shopping.Mercado.Repository;

import com.shopping.Mercado.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>{
    Category findByName(String name);
}
