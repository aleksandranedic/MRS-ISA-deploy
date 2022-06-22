package com.project.team9.dto;

import com.project.team9.model.Tag;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.user.Client;

import java.util.List;

public class ClientReservationDTO extends ReservationDTO {
    private String entityType;

    public ClientReservationDTO(String entityType) {
        this.entityType = entityType;
    }

    public ClientReservationDTO(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, String resourceTitle, boolean isBusyPeriod, boolean isQuickReservation, Long resourceId, String entityType) {
        super(appointments, numberOfClients, additionalServices, price, client, resourceTitle, isBusyPeriod, isQuickReservation, resourceId, entityType);
        this.entityType = entityType;
    }

    public ClientReservationDTO(List<Appointment> appointments, int numberOfClients, List<Tag> additionalServices, int price, Client client, String resourceTitle, boolean isBusyPeriod, boolean isQuickReservation, Long resourceId, Long id, String entityType) {
        super(appointments, numberOfClients, additionalServices, price, client, resourceTitle, isBusyPeriod, isQuickReservation, resourceId, id, entityType);
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
