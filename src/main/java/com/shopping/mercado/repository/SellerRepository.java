package com.shopping.mercado.repository;

import com.shopping.mercado.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Optional<Seller> findByUserUserId(UUID userId);
    Optional<Seller> findByGstNumber(String gstNumber);
    Optional<Seller> findByStoreName(String storeName);
    boolean existsByStoreNameAndSellerIdNot(String storeName, UUID sellerId);
}
