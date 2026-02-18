package com.shopping.Mercado.Service;

import com.shopping.Mercado.Dto.UserDTO.CreateUserRequest;
import com.shopping.Mercado.Dto.UserDTO.UserResponse;
import com.shopping.Mercado.Entity.User;
import com.shopping.Mercado.Entity.UserPrincipal;
import com.shopping.Mercado.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User  user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(user);
    }

    public UserResponse createUser(CreateUserRequest dto) {
        boolean checkExistingUser = userRepository.existsByEmailOrPhoneNumber(dto.email, dto.phoneNumber);

        if (checkExistingUser)
            throw new RuntimeException("User already exists");

        User user = new User();
        user.setUsername(dto.username);
        user.setPassword(dto.password);
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
}
