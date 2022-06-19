package com.project.team9.model.reservation;

import com.project.team9.model.Tag;
import com.project.team9.model.user.Client;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public class Reservation {
    @Id
    @SequenceGenerator(
            name = "reservation_sequence",
            sequenceName = "reservation_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_sequence"
    )
    private Long id;
    @OneToMany
    List<Appointment> appointments;
    private int numberOfClients;
    @OneToMany
    private List<Tag> additionalServices;
    private int price;  //It is possible that the price is lower that the sum of appointment prices if the reservation is quick reservation
    @ManyToOne
    private Client client;
    private boolean isBusyPeriod;
    private boolean isQuickReservation;
    private boolean deleted=false;
    private boolean confirmed=false;

    public Reservation() {
    }

    public Reservation(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, boolean isBusyPeriod, boolean isQuickReservation) {
        this.appointments = appointments;
        this.numberOfClients = numberOfClients;
        this.additionalServices = additionalServices;
        this.price = price;
        this.client = client;
        this.isBusyPeriod = isBusyPeriod;
        this.isQuickReservation = isQuickReservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public List<Tag> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<Tag> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isBusyPeriod() {
        return isBusyPeriod;
    }

    public void setBusyPeriod(boolean busyPeriod) {
        isBusyPeriod = busyPeriod;
    }

    public boolean isQuickReservation() {
        return isQuickReservation;
    }

    public void setQuickReservation(boolean quickReservation) {
        isQuickReservation = quickReservation;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
