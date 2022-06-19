package com.project.team9.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.team9.model.Address;
import com.project.team9.model.Image;

import javax.persistence.Entity;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client extends User {

    private int numOfPenalties=0;
    private int numOfPoints=0;

    public Client(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, Address address, Boolean deleted, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, address,  deleted, role);
    }

    public Client(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, place, number, street, country,  deleted, role);
    }

    public Client(String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, Role role) {
        super(null, password, firstName, lastName, email, phoneNumber, place, number, street, country,  deleted, role);
    }

    public Client() {
        super();
    }

    public int getNumOfPenalties() {
        return numOfPenalties;
    }

    public void setNumOfPenalties(int numOfPenalties) {
        this.numOfPenalties = numOfPenalties;
    }

    public int getNumOfPoints() {
        return numOfPoints;
    }

    public void setNumOfPoints(int numOfPoints) {
        this.numOfPoints = numOfPoints;
    }

    public void addPoints(int points) {
        this.numOfPoints += points;
    }
}
