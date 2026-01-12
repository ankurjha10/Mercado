package com.shopping.Mercado.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID Product_Id;

    @NotBlank
    private String productName;

    private String productDescription;

    @NotBlank
    private String productCategory;

    @Min(0)
    private int productQuantity;

    @PositiveOrZero
    private double productPrice;

    @NotBlank
    private String productImage;

    @CreationTimestamp
    private LocalDate productDate;
}
