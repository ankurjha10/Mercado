package com.shopping.mercado.dto.seller;

import lombok.Data;

@Data
public class SellerProfileRequest {
    private String storeName;
    private String storeLogoUrl;
    private String storeDescription;
    private String phoneNumber;
    private String gstNumber;
}
