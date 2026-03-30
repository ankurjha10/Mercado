package com.shopping.mercado.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerProfileResponse {
    private UUID sellerId;
    private String storeName;
    private String storeDescription;
    private String storeLogoUrl;
    private String phoneNumber;
    private String gstNumber;
    private boolean isVerified;
}
