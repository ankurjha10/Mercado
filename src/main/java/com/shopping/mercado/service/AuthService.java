package com.shopping.mercado.service;

import com.shopping.mercado.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopping.mercado.dto.auth.LoginRequest;
import com.shopping.mercado.dto.auth.LoginResponse;
import com.shopping.mercado.dto.auth.RegisterRequest;
import com.shopping.mercado.dto.auth.RegisterResponse;
import com.shopping.mercado.entity.User;
import com.shopping.mercado.entity.UserPrincipal;
import com.shopping.mercado.entity.enums.UserRole;
import com.shopping.mercado.repository.UserRepository;
import com.shopping.mercado.security.AuthUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest signUpRequest) {
        // Validate input
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (signUpRequest.getEmail() == null || signUpRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (signUpRequest.getPhoneNumber() == null || signUpRequest.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        // Check if user already exists
        boolean userExists = userRepository.existsByEmailOrPhoneNumber(
                signUpRequest.getEmail().trim(),
                signUpRequest.getPhoneNumber().trim()
        );

        if (userExists) {
            throw new IllegalArgumentException("User already exists with this email or phone number");
        }

        // Check if username already taken
        if (userRepository.findByUsername(signUpRequest.getUsername().trim()) != null) {
            throw new IllegalArgumentException("Username already taken");
        }

        try {
            User newUser = User.builder()
                    .username(signUpRequest.getUsername().trim())
                    .email(signUpRequest.getEmail().trim())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .phoneNumber(signUpRequest.getPhoneNumber().trim())
                    .role(signUpRequest.getRole() != null ? signUpRequest.getRole() : UserRole.CUSTOMER)
                    .isActive(true)
                    .build();


            User savedUser = userRepository.save(newUser);

            if (newUser.getRole().equals(UserRole.SELLER)) {
                Seller seller = new Seller();
                seller.setUser(savedUser);
                seller.setStoreName("DefaultStore");
                seller.setStoreDescription("Add Store Description");
            }

            RegisterResponse res = new RegisterResponse();
            res.setId(savedUser.getUserId());
            res.setUsername(savedUser.getUsername());
            res.setEmail(savedUser.getEmail());
            res.setPhoneNumber(savedUser.getPhoneNumber());
            res.setRole(savedUser.getRole());

            return res;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during user registration: " + e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername().trim(),
                            loginRequest.getPassword()
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();
            String token = authUtil.generateAccessToken(user);

            return new LoginResponse(token, user.getUserId());
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            throw new IllegalArgumentException("User not found");
        } catch (Exception e) {
            throw new IllegalArgumentException("Login failed: " + e.getMessage());
        }
    }
}
