package com.example.wgu.finance_tracker_backend.DTOs;

public class UserLoginResponse {
    private String token;
    private String username;

    public UserLoginResponse() {
    }

    public UserLoginResponse(String token, String userName) {
        this.token = token;
        this.username = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
