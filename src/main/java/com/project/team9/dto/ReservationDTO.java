package com.project.team9.dto;

import com.project.team9.model.Tag;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.user.Client;

import java.util.List;

public class ReservationDTO {
    List<Appointment> appointments;
    int numberOfClients;
    List<Tag> additionalServices;
    int price;
    Client client;
    String resourceTitle;
    Long resourceId;
    boolean isBusyPeriod;
    boolean isQuickReservation;
    Long id;

    public ReservationDTO() {
    }

    public ReservationDTO(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, String resourceTitle, boolean isBusyPeriod, boolean isQuickReservation, Long resourceId) {
        this.appointments = appointments;
        this.numberOfClients = numberOfClients;
        this.additionalServices = additionalServices;
        this.price = price;
        this.client = client;
        this.resourceTitle = resourceTitle;
        this.isBusyPeriod = isBusyPeriod;
        this.isQuickReservation = isQuickReservation;
        this.resourceId = resourceId;
    }

    public ReservationDTO(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, String resourceTitle, boolean isBusyPeriod, boolean isQuickReservation, Long resourceId, Long id) {
        this.appointments = appointments;
        this.numberOfClients = numberOfClients;
        this.additionalServices = additionalServices;
        this.price = price;
        this.client = client;
        this.resourceTitle = resourceTitle;
        this.resourceId = resourceId;
        this.isBusyPeriod = isBusyPeriod;
        this.isQuickReservation = isQuickReservation;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
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

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
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
}
