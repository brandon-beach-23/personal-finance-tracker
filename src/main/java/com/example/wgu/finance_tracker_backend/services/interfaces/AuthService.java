package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.LoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
}
