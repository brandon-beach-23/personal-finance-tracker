package com.example.wgu.finance_tracker_backend.DTOs;

public class LoginResponse {

    private String token;
    private Long userId;
    private String userName;
    private String userEmail;

    public LoginResponse(String token, Long userId, String userName, String userEmail) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
