package com.shopping.mercado.controller;

import com.shopping.mercado.dto.seller.SellerProfileRequest;
import com.shopping.mercado.dto.seller.SellerProfileResponse;
import com.shopping.mercado.entity.UserPrincipal;
import com.shopping.mercado.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<SellerProfileResponse> createSellerProfile(@RequestBody @Valid SellerProfileRequest sellerProfileRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.getUser().getUserId();
        return ResponseEntity.ok(sellerService.createSeller(sellerProfileRequest, userId));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<SellerProfileResponse> getSellerProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.getUser().getUserId();
        return ResponseEntity.ok(sellerService.getSellerProfile(userId));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<SellerProfileResponse> updateSellerProfile(@RequestBody SellerProfileRequest sellerProfileRequest, @AuthenticationPrincipal UserPrincipal userPrincipal){
        UUID userId = userPrincipal.getUser().getUserId();
        return ResponseEntity.ok(sellerService.updateSellerProfile(userId, sellerProfileRequest));
    }

//    @GetMapping("/orders")
//    @PreAuthorize("hasRole('SELLER')")
//    public ResponseEntity
}
