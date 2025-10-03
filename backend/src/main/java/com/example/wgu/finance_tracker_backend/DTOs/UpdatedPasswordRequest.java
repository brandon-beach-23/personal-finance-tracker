package com.example.wgu.finance_tracker_backend.DTOs;

import jakarta.validation.constraints.NotBlank;

public class UpdatedPasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    public UpdatedPasswordRequest() {
    }

    public UpdatedPasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
