package com.project.team9.dto;

public class LoginResponseDTO {
    private boolean isConfirmed;
    private String jwt;
    private String roleName;
    private Long id;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String jwt, String roleName, Long id, boolean isConfirmed) {
        this.jwt = jwt;
        this.roleName = roleName;
        this.id = id;
        this.isConfirmed = isConfirmed;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}

