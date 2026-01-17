package com.shopping.Mercado.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID productId;

    @NotBlank
    private String productName;

    private String productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    @NotNull
    private Category productCategory;

    @Min(0)
    private int productQuantity;

    @PositiveOrZero
    private BigDecimal productPrice;

    @NotBlank
    private String productImage;

    @CreationTimestamp
    private LocalDate productDate;
}
