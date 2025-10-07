package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserLoginResponse;
import com.example.wgu.finance_tracker_backend.DTOs.UserRegistrationRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserResponse;
import com.example.wgu.finance_tracker_backend.exceptions.InvalidCredentialsException;
import com.example.wgu.finance_tracker_backend.exceptions.InvalidRegistrationDataException;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.exceptions.UserAlreadyExistsException;
import com.example.wgu.finance_tracker_backend.models.User;
import com.example.wgu.finance_tracker_backend.repositories.UserRepository;
import com.example.wgu.finance_tracker_backend.security.JwtUtil;
import com.example.wgu.finance_tracker_backend.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {

        if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email address " + userRegistrationRequest.getEmail() + " already exists");
        }

        if (userRepository.findByUserName(userRegistrationRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with name " + userRegistrationRequest.getUsername() + " already exists");
        }

        User user = new User();
        user.setEmail(userRegistrationRequest.getEmail());
        user.setUserName(userRegistrationRequest.getUsername());

        String hashedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());

        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest loginRequest) {
       if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

       User user = userRepository.findByUserName(loginRequest.getUsername())
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

       if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
           throw new InvalidCredentialsException("Invalid credentials");
       } else {
           String token = jwtUtil.createJwtToken(user);
           UserLoginResponse userLoginResponse = new UserLoginResponse();
           userLoginResponse.setToken(token);
           userLoginResponse.setUsername(user.getUserName());
           return userLoginResponse;
       }

    }

    public UserResponse convertToDTO(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUserName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
