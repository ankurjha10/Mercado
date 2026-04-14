package com.shopping.mercado.repository;

import com.shopping.mercado.entity.Orders;
import com.shopping.mercado.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    List<Orders> findByCustomer_UserId(UUID customer);

}
