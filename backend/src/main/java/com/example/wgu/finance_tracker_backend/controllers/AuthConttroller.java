package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserLoginResponse;
import com.example.wgu.finance_tracker_backend.DTOs.UserRegistrationRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserResponse;
import com.example.wgu.finance_tracker_backend.exceptions.InvalidCredentialsException;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.exceptions.UserAlreadyExistsException;
import com.example.wgu.finance_tracker_backend.services.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthConttroller {

    private final AuthService authService;

    @Autowired
    public AuthConttroller(AuthService authService) {
        this.authService = authService;
    }


    // AUTH: POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            UserResponse userResponse = authService.registerUser(registrationRequest);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    //    // AUTH: POST /api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        try {
            UserLoginResponse userLoginResponse = authService.login(loginRequest);
            return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidCredentialsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Login failed due to server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
