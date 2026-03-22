package com.shopping.mercado.dto.auth;

import com.shopping.mercado.entity.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private UserRole role;
}
