package com.shopping.Mercado.Controller;

import com.shopping.Mercado.Dto.UserDTO.CreateUserRequest;
import com.shopping.Mercado.Dto.UserDTO.UserResponse;
import com.shopping.Mercado.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add-user")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest user){
        return ResponseEntity.ok(userService.createUser(user));
    }

}
