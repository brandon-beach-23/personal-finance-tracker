package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserRegistrationRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserResponse;
import com.example.wgu.finance_tracker_backend.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    Optional<UserResponse> login(UserLoginRequest userLoginRequest);
    void updatePassword(Long userId, String newPassword);
    void deleteUser(Long userId);

}
