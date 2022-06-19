package com.project.team9.dto;

public class PasswordsDTO {
    private String oldPassword;
    private String newPassword;

    public PasswordsDTO() {
    }

    public PasswordsDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
