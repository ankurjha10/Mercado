package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Dto.AuthDTO.RegisterRequest;
import com.shopping.Mercado.Dto.AuthDTO.LoginRequest;
import com.shopping.Mercado.Dto.AuthDTO.LoginResponse;
import com.shopping.Mercado.Dto.AuthDTO.RegisterResponse;
import com.shopping.Mercado.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest user){
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user){
        System.out.println("Login attempt for user: " + user.username);
        return ResponseEntity.ok(authService.login(user));
    }
}
