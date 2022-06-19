package com.project.team9.model.request;

import javax.persistence.Entity;
import javax.persistence.Version;


@Entity
public class RegistrationRequest extends Request{

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String place;
    private String number;
    private String street;
    private String country;
    private String userRole;
    private String biography;
    private String registrationRationale;

    @Version
    private Long version;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String text, String response, String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, String userRole, String biography, String registrationRationale) {
        super(text, response);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.place = place;
        this.number = number;
        this.street = street;
        this.country = country;
        this.userRole = userRole;
        this.biography = biography;
        this.registrationRationale = registrationRationale;
    }

    public String getRegistrationRationale() {
        return registrationRationale;
    }

    public void setRegistrationRationale(String registrationRationale) {
        this.registrationRationale = registrationRationale;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
