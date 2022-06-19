package com.project.team9.model.user.vendor;

import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.user.Role;
import com.project.team9.model.user.User;

import javax.persistence.MappedSuperclass;
import java.util.List;

@MappedSuperclass
public class Vendor extends User {
    private String registrationRationale;

    private int numOfPoints=0;

    public Vendor() {
    }

    public Vendor(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, Address address, Boolean deleted, String registrationRationale, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, address,  deleted, role);
        this.registrationRationale = registrationRationale;
        numOfPoints = 0;
    }

    public Vendor(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, String registrationRationale, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, place, number, street, country, deleted, role);
        this.registrationRationale = registrationRationale;
        numOfPoints = 0;
    }

    public Vendor(String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, String registrationRationale, Role role) {
        super(null, password, firstName, lastName, email, phoneNumber, place, number, street, country, deleted, role);
        this.registrationRationale = registrationRationale;
        numOfPoints = 0;
    }

    public String getRegistrationRationale() {
        return registrationRationale;
    }

    public void setRegistrationRationale(String registrationRationale) {
        this.registrationRationale = registrationRationale;
    }

    public int getNumOfPoints() {
        return numOfPoints;
    }

    public void setNumOfPoints(int numOfPoints) {
        this.numOfPoints = numOfPoints;
    }
}
