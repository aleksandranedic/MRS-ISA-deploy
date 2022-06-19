package com.project.team9.model.user;

import com.project.team9.model.Address;
import com.project.team9.model.Image;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Administrator extends User {

    private boolean isSuperAdministrator;

    public Administrator() {
    }

    public Administrator(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, Address address, Boolean deleted, Role role, boolean isSuperAdministrator) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, address,  deleted, role);
        this.isSuperAdministrator = isSuperAdministrator;
    }


    public Administrator(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, place, number, street, country,  deleted, role);
    }

    public Administrator(String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, Role role) {
        super(password, firstName, lastName, email, phoneNumber, place, number, street, country,  deleted, role);

    }

    public boolean isSuperAdministrator() {
        return isSuperAdministrator;
    }

    public void setSuperAdministrator(boolean superAdministrator) {
        isSuperAdministrator = superAdministrator;
    }
}
