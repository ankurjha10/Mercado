package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Dto.AuthDTO.CreateUserRequest;
import com.shopping.Mercado.Dto.AuthDTO.LoginRequestDTO;
import com.shopping.Mercado.Dto.AuthDTO.LoginResponseDTO;
import com.shopping.Mercado.Dto.AuthDTO.UserResponse;
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
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest user){
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO user){
        System.out.println("Login attempt for user: " + user.username);
        return ResponseEntity.ok(authService.login(user));
    }
}
