package com.shopping.mercado.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private UUID sellerId;
    private String storeName;
    private String storeDescription;
    private String storeLogoUrl;
    private String gstNumber;
    private boolean isVerified;
    private String phoneNumber;

    private String ownerName;
    private String ownerEmail;
}
