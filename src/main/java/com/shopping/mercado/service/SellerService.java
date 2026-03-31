package com.shopping.mercado.service;

import com.shopping.mercado.dto.seller.SellerProfileRequest;
import com.shopping.mercado.dto.seller.SellerProfileResponse;
import com.shopping.mercado.entity.Seller;
import com.shopping.mercado.entity.User;
import com.shopping.mercado.repository.SellerRepository;
import com.shopping.mercado.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
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

        if (sellerRepository.findByStoreName(request.getStoreName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Store name already exists");
        }

        if (sellerRepository.findByGstNumber(request.getGstNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "GST already exists");
        }

        SellerProfileResponse response;
        try {
            Seller savedSeller = sellerRepository.save(seller);

            response = new SellerProfileResponse();
            response.setSellerId(savedSeller.getSellerId());
            response.setStoreName(savedSeller.getStoreName());
            response.setStoreLogoUrl(savedSeller.getStoreLogoUrl());
            response.setStoreDescription(savedSeller.getStoreDescription());
            response.setPhoneNumber(savedSeller.getPhoneNumber());
            response.setGstNumber(savedSeller.getGstNumber());
            response.setVerified(savedSeller.isVerified());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate or Invalid Data");
        }

        return response;
    }

    @Transactional
    public SellerProfileResponse getSellerProfile(UUID userId) {
        Seller seller = sellerRepository.findByUserUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!seller.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller not verified, Kindly wait for approval");
        }

        SellerProfileResponse response;
        try {
            response = new SellerProfileResponse();
            response.setSellerId(seller.getSellerId());
            response.setStoreName(seller.getStoreName());
            response.setStoreLogoUrl(seller.getStoreLogoUrl());
            response.setStoreDescription(seller.getStoreDescription());
            response.setPhoneNumber(seller.getPhoneNumber());
            response.setGstNumber(seller.getGstNumber());
            response.setVerified(seller.isVerified());

            return response;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
    }

    @Transactional
    public SellerProfileResponse updateSellerProfile(UUID userId, SellerProfileRequest request) {
        Seller seller = sellerRepository.findByUserUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

        if (sellerRepository.existsByStoreNameAndSellerIdNot(request.getStoreName(), seller.getSellerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Store name already exists");
        }

        seller.setStoreName(request.getStoreName());
        seller.setStoreLogoUrl(request.getStoreLogoUrl());
        seller.setStoreDescription(request.getStoreDescription());
        seller.setPhoneNumber(request.getPhoneNumber());
        seller.setGstNumber(request.getGstNumber());

        return new SellerProfileResponse(
                seller.getSellerId(),
                seller.getStoreName(),
                seller.getStoreDescription(),
                seller.getStoreLogoUrl(),
                seller.getPhoneNumber(),
                seller.getGstNumber(),
                seller.isVerified()
        );
    }
}