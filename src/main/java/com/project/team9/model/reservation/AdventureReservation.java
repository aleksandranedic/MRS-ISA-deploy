package com.project.team9.model.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.team9.model.Tag;
import com.project.team9.model.resource.Adventure;
import com.project.team9.model.user.Client;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.util.List;

@Entity
public class AdventureReservation extends Reservation{
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    private Adventure resource;
    @Version
    private Long version;
    public AdventureReservation() {
    }

    public AdventureReservation(List<Appointment> appointments,
                                int numberOfClients,
                                List<Tag> additionalServices,
                                int price,
                                Client client,
                                Adventure resource,
                                boolean isBusyPeriod,
                                boolean isQuickReservation) {
        super(appointments, numberOfClients, additionalServices, price, client, isBusyPeriod, isQuickReservation);
        this.resource = resource;
    }

    public AdventureReservation(int numberOfClients, int price) {
        super(null, numberOfClients, null, price, null, false, false);
        this.resource = null;
    }

    @JsonBackReference
    public Adventure getResource() {
        return resource;
    }

    public void setResource(Adventure adventure) {
        this.resource = adventure;
    }

    @Override
    public String toString() {
        return "AdventureReservation{" +
                "resource=" + resource +
                ", appointments=" + appointments +
                '}';
    }
}
