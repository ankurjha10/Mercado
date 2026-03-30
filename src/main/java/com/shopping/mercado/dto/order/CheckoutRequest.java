package com.shopping.mercado.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {

    @NotNull(message = "Select an Address")
    private UUID addressId;
}
