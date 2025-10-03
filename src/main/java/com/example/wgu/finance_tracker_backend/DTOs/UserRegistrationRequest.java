package com.example.wgu.finance_tracker_backend.DTOs;

import com.example.wgu.finance_tracker_backend.validators.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegistrationRequest {
    @NotBlank
    private String username;
    @NotBlank
    @ValidPassword
    private String password;
    @NotBlank
    @Email
    private String email;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
