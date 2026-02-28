package com.shopping.Mercado.Dto.AuthDTO;

import com.shopping.Mercado.Entity.enums.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    public UUID id;
    public String username;
    public String email;
    public String phoneNumber;
    public UserRole role;
}
