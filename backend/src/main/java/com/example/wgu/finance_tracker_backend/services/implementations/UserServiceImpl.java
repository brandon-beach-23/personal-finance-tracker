package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserRegistrationRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserResponse;
import com.example.wgu.finance_tracker_backend.config.SecurityConfig;
import com.example.wgu.finance_tracker_backend.exceptions.InvalidRegistrationDataException;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.exceptions.UserAlreadyExistsException;
import com.example.wgu.finance_tracker_backend.models.User;
import com.example.wgu.finance_tracker_backend.repositories.UserRepository;
import com.example.wgu.finance_tracker_backend.services.interfaces.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {

        if (userRegistrationRequest == null || userRegistrationRequest.getEmail() == null || userRegistrationRequest.getPassword() == null || userRegistrationRequest.getUsername() == null) {
           throw new InvalidRegistrationDataException("UserRegistrationRequest is null or empty");
       }

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

       UserResponse userResponse = new UserResponse();
       userResponse.setId(savedUser.getId());
       userResponse.setUsername(savedUser.getUserName());
       userResponse.setEmail(savedUser.getEmail());

       return userResponse;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
