package com.project.team9.model.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.team9.model.Tag;
import com.project.team9.model.resource.VacationHouse;
import com.project.team9.model.user.Client;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.util.List;

@Entity
public class VacationHouseReservation extends Reservation{

    @ManyToOne
    private VacationHouse resource;
    @Version
    private Long version;
    public VacationHouseReservation() {
    }

    public VacationHouseReservation(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, VacationHouse vacationHouse, boolean isBusyPeriod, boolean isQuickReservation) {
        super(appointments, numberOfClients, additionalServices, price, client, isBusyPeriod, isQuickReservation);
        this.resource = vacationHouse;
    }
    public VacationHouseReservation(int numberOfClients, int price) {
        super(null, numberOfClients, null, price, null, false, false);
        this.resource = null;
    }

    @JsonBackReference
    public VacationHouse getResource() {
        return resource;
    }

    public void setResource(VacationHouse vacationHouse) {
        this.resource = vacationHouse;
    }
}
