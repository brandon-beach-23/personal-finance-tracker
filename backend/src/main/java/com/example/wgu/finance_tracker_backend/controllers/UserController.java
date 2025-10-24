package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserLoginResponse;
import com.example.wgu.finance_tracker_backend.DTOs.UserRegistrationRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserResponse;
import com.example.wgu.finance_tracker_backend.exceptions.UserAlreadyExistsException;
import com.example.wgu.finance_tracker_backend.services.interfaces.AuthService;
import com.example.wgu.finance_tracker_backend.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // Dependency Injection via Constructor
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    // MANAGEMENT: DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
