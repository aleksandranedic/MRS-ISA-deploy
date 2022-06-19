package com.project.team9.dto;

import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.user.Role;
import com.project.team9.model.user.User;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Image profileImg;
    private Address address;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRoleName();
        this.profileImg = user.getProfileImg();
        this.address = user.getAddress();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Image getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(Image profileImg) {
        this.profileImg = profileImg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
