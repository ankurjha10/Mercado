package com.shopping.mercado.service;

import com.shopping.mercado.dto.admin.SellerResponse;
import com.shopping.mercado.dto.admin.UserResponse;
import com.shopping.mercado.entity.Seller;
import com.shopping.mercado.entity.User;
import com.shopping.mercado.repository.ProductRepository;
import com.shopping.mercado.repository.SellerRepository;
import com.shopping.mercado.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    public List<SellerResponse> getAllSellers(){
        return sellerRepository.findAll().stream().map(this::toSellerResponse).toList();
    }

    public SellerResponse getSellerById(UUID sellerId){
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not Found with Id: " + sellerId));
        return toSellerResponse(seller);
    }

    public void verifySeller(UUID sellerId){
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not Found with Id: " + sellerId));

        seller.setVerified(true);
        sellerRepository.save(seller);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private SellerResponse toSellerResponse(Seller seller){
        return SellerResponse.builder()
                .sellerId(seller.getSellerId())
                .storeName(seller.getStoreName())
                .storeDescription(seller.getStoreDescription())
                .storeLogoUrl(seller.getStoreLogoUrl())
                .gstNumber(seller.getGstNumber())
                .isVerified(seller.isVerified())
                .phoneNumber(seller.getPhoneNumber())
                .ownerName(seller.getUser().getUsername())
                .ownerEmail(seller.getUser().getEmail())
                .build();
    }
}
