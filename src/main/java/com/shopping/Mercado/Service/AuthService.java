package com.shopping.Mercado.Service;

import com.shopping.Mercado.Dto.AuthDTO.CreateUserRequest;
import com.shopping.Mercado.Dto.AuthDTO.LoginRequestDTO;
import com.shopping.Mercado.Dto.AuthDTO.UserResponse;
import com.shopping.Mercado.Entity.User;
import com.shopping.Mercado.Repository.UserRepository;
import com.shopping.Mercado.Security.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponse register(CreateUserRequest dto) {
        boolean checkExistingUser = userRepository.existsByEmailOrPhoneNumber(dto.email, dto.phoneNumber);

        if (checkExistingUser)
            throw new RuntimeException("User already exists");

        User user = new User();
        user.setUsername(dto.username);
        user.setPassword(passwordEncoder.encode(dto.password));
        user.setEmail(dto.email);
        user.setPhoneNumber(dto.phoneNumber);
        user.setRole(dto.role);

        User savedUser = userRepository.save(user);

        UserResponse res = new UserResponse();
        res.id = savedUser.getId();
        res.username = savedUser.getUsername();
        res.email = savedUser.getEmail();
        res.phoneNumber = savedUser.getPhoneNumber();
        res.role = savedUser.getRole();

        return res;
    }

    public String login(LoginRequestDTO user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.username, user.password));

        if(authentication.isAuthenticated()) {
            return jwtUtil.generateToken(user.username);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
