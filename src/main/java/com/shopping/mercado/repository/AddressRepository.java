package com.shopping.mercado.repository;

import com.shopping.mercado.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> { // ✅ UUID

    List<Address> findAllByUserUserId(UUID userId);

    Optional<Address> findByUserUserIdAndIsDefaultTrue(UUID userId);
}
