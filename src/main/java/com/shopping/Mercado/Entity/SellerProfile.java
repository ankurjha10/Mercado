package com.shopping.Mercado.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seller_profile")
public class SellerProfile {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID sellerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true)
    private String storeName;

    private String storeDescription;

    private String storeLogoUrl;

    @Column(unique = true)
    private String gstNumber;

    private boolean isVerified;

    @CreationTimestamp
    private Date createdAt;
}