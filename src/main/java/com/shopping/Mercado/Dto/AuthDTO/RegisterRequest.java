package com.shopping.Mercado.Dto.AuthDTO;

import com.shopping.Mercado.Entity.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    public String username;
    public String email;
    public String password;
    public String phoneNumber;
    public UserRole role;
}
