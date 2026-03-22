package com.shopping.mercado.controller;

import com.shopping.mercado.dto.auth.RegisterRequest;
import com.shopping.mercado.dto.auth.LoginRequest;
import com.shopping.mercado.dto.auth.LoginResponse;
import com.shopping.mercado.dto.auth.RegisterResponse;
import com.shopping.mercado.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest user){
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user){
        return ResponseEntity.ok(authService.login(user));
    }
}
