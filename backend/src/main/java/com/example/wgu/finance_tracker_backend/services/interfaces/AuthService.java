package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserLoginResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthService {

    UserLoginResponse login(UserLoginRequest loginRequest);
}
