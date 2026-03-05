package com.shopping.Mercado.Service;

import com.shopping.Mercado.Dto.AuthDTO.RegisterRequest;
import com.shopping.Mercado.Dto.AuthDTO.LoginRequest;
import com.shopping.Mercado.Dto.AuthDTO.LoginResponse;
import com.shopping.Mercado.Dto.AuthDTO.RegisterResponse;
import com.shopping.Mercado.Entity.User;
import com.shopping.Mercado.Entity.UserPrincipal;
import com.shopping.Mercado.Repository.UserRepository;
import com.shopping.Mercado.Security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest signUpRequest) {
        // Validate input
        if (signUpRequest.username == null || signUpRequest.username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (signUpRequest.email == null || signUpRequest.email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (signUpRequest.password == null || signUpRequest.password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (signUpRequest.phoneNumber == null || signUpRequest.phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        // Check if user already exists
        boolean userExists = userRepository.existsByEmailOrPhoneNumber(
                signUpRequest.email.trim(),
                signUpRequest.phoneNumber.trim()
        );

        if (userExists) {
            throw new IllegalArgumentException("User already exists with this email or phone number");
        }

        // Check if username already taken
        if (userRepository.findByUsername(signUpRequest.username.trim()) != null) {
            throw new IllegalArgumentException("Username already taken");
        }

        try {
            User newUser = User.builder()
                    .username(signUpRequest.username.trim())
                    .email(signUpRequest.email.trim())
                    .password(passwordEncoder.encode(signUpRequest.password))
                    .phoneNumber(signUpRequest.phoneNumber.trim())
                    .role(signUpRequest.role != null ? signUpRequest.role : com.shopping.Mercado.Entity.enums.UserRole.CUSTOMER)
                    .isActive(true)
                    .build();

            User savedUser = userRepository.save(newUser);

            RegisterResponse res = new RegisterResponse();
            res.id = savedUser.getUserId();
            res.username = savedUser.getUsername();
            res.email = savedUser.getEmail();
            res.phoneNumber = savedUser.getPhoneNumber();
            res.role = savedUser.getRole();

            return res;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during user registration: " + e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            if (loginRequest.username == null || loginRequest.username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (loginRequest.password == null || loginRequest.password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username.trim(),
                            loginRequest.password
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
