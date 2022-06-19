package com.project.team9.model.user.vendor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.resource.VacationHouse;
import com.project.team9.model.user.Role;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VacationHouseOwner extends Vendor {
    @OneToMany
    List<VacationHouse> vacationHouses;

    public VacationHouseOwner() {
        vacationHouses = new ArrayList<VacationHouse>();
    }

    public VacationHouseOwner(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, Address address, Boolean deleted, String registrationRationale, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, address, deleted, registrationRationale, role);
        vacationHouses = new ArrayList<>();
        this.setEnabled(true);
    }

    public VacationHouseOwner(Image profileImg, String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, String registrationRationale, Role role) {
        super(profileImg, password, firstName, lastName, email, phoneNumber, place, number, street, country, deleted, registrationRationale, role);
        vacationHouses = new ArrayList<>();
        this.setEnabled(true);
    }

    public VacationHouseOwner(String password, String firstName, String lastName, String email, String phoneNumber, String place, String number, String street, String country, Boolean deleted, String registrationRationale, Role role) {
        super(null, password, firstName, lastName, email, phoneNumber, place, number, street, country, deleted, registrationRationale, role);
        vacationHouses = new ArrayList<>();
        this.setEnabled(true);
    }


    public void addVacationHouse(VacationHouse vacationHouse) {
        this.vacationHouses.add(vacationHouse);
    }

    public void removeVacationHouse(VacationHouse vacationHouse) {
        this.vacationHouses.remove(vacationHouse);
    }

    public VacationHouse getVacationHouse(int id) {
        for (VacationHouse b : vacationHouses) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }


}
