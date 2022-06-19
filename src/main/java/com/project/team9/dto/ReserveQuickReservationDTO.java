package com.project.team9.dto;

public class ReserveQuickReservationDTO {

    private Long reservationID;
    private Long clientID;

    public ReserveQuickReservationDTO() {
    }

    public ReserveQuickReservationDTO(Long reservationId, Long clientId) {
        this.reservationID = reservationId;
        this.clientID = clientId;
    }

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }
}
