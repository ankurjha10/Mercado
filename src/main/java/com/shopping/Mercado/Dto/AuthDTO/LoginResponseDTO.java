package com.shopping.Mercado.Dto.AuthDTO;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponseDTO {
    public String token;
    public UUID id;
}
