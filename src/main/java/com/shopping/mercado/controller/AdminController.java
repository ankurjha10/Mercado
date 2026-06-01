package com.shopping.mercado.controller;

import com.shopping.mercado.dto.admin.SellerResponse;
import com.shopping.mercado.dto.admin.UserResponse;
import com.shopping.mercado.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SellerResponse>> getAllSellers() {
        return ResponseEntity.ok(adminService.getAllSellers());
    }

    @GetMapping("/sellers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SellerResponse> getSellerById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getSellerById(id));
    }

    @PatchMapping("/sellers/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifySeller(@PathVariable UUID id) {
        adminService.verifySeller(id);
        return ResponseEntity.ok("Seller verified");
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted");
    }
}
