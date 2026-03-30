package com.shopping.mercado.service;

import com.shopping.mercado.dto.seller.SellerProfileRequest;
import com.shopping.mercado.dto.seller.SellerProfileResponse;
import com.shopping.mercado.entity.Seller;
import com.shopping.mercado.entity.User;
import com.shopping.mercado.repository.SellerRepository;
import com.shopping.mercado.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public SellerProfileResponse createSeller(SellerProfileRequest request, UUID userId) {

        if (sellerRepository.findByUserUserId(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Seller Profile already exists");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Seller seller = Seller.builder()
                .user(user)
                .storeName(request.getStoreName())
                .storeDescription(request.getStoreDescription())
                .storeLogoUrl(request.getStoreLogoUrl())
                .phoneNumber(request.getPhoneNumber())
                .gstNumber(request.getGstNumber())
                .isVerified(false)
                .build();

        Seller savedSeller = sellerRepository.save(seller);

        SellerProfileResponse response = new SellerProfileResponse();
        response.setSellerId(savedSeller.getSellerId());
        response.setStoreName(savedSeller.getStoreName());
        response.setStoreLogoUrl(savedSeller.getStoreLogoUrl());
        response.setStoreDescription(savedSeller.getStoreDescription());
        response.setPhoneNumber(savedSeller.getPhoneNumber());
        response.setGstNumber(savedSeller.getGstNumber());
        response.setVerified(savedSeller.isVerified());

        return response;
    }
}
