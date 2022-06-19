package com.project.team9.model.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.team9.model.Tag;
import com.project.team9.model.resource.Boat;
import com.project.team9.model.user.Client;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BoatReservation extends Reservation{
    @ManyToOne
    private Boat resource;
    @Version
    private Long version;
    public BoatReservation() {
    }

    public BoatReservation(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, Boat boat, boolean isBusyPeriod, boolean isQuickReservation) {
        super(appointments, numberOfClients, additionalServices, price, client, isBusyPeriod, isQuickReservation);
        this.resource = boat;
    }

    public BoatReservation(int numberOfClients, int price) {
        super(null, numberOfClients, null, price, null, false, false);
        this.resource = null;
    }

    @JsonBackReference
    public Boat getResource() {
        return resource;
    }

    public void setResource(Boat boat) {
        this.resource = boat;
    }
}
