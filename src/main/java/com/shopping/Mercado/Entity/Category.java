package com.shopping.Mercado.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "category_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID categoryId;

    @NotBlank
    @Column(name = "category_name", nullable = false, length = 100, unique = true)
    private String categoryName;

    @NotBlank
    @Column(name = "category_description", nullable = false)
    private String categoryDescription;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> products;
}
