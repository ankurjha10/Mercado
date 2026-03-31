package com.shopping.mercado.dto.seller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SellerProfileRequest {

    @NotBlank
    private String storeName;

    @NotBlank
    private String storeLogoUrl;

    @NotBlank
    private String storeDescription;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String gstNumber;
}
