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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        boolean checkExistingUser = userRepository.existsByEmailOrPhoneNumber(signUpRequest.email, signUpRequest.phoneNumber);

        if (checkExistingUser)
            throw new IllegalArgumentException("User already exists");

        User savedUser = userRepository.save(User.builder()
                .username(signUpRequest.username)
                .email(signUpRequest.email)
                .password(passwordEncoder.encode(signUpRequest.password))
                .phoneNumber(signUpRequest.phoneNumber)
                .role(signUpRequest.role)
                .isActive(true)
                .build()
        );

        RegisterResponse res = new RegisterResponse();
        res.id = savedUser.getId();
        res.username = savedUser.getUsername();
        res.email = savedUser.getEmail();
        res.phoneNumber = savedUser.getPhoneNumber();
        res.role = savedUser.getRole();

        return res;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password));

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        String token = authUtil.generateAccessToken(user);

        return new LoginResponse(token, user.getId());
    }
}
