package com.project.team9.model.review;

import javax.persistence.Entity;

@Entity
public class VendorReview extends Review{

    private Long clientId;
    private boolean penalty;
    private boolean noShow;
    private Long reservationId;
    private String response;


    public VendorReview() {
    }

    public VendorReview(Long resourceId, Long vendorId, int rating, String text, Long clientId, boolean penalty, boolean noShow, Long reservationId, String response) {
        super(resourceId, vendorId, rating, text, response);
        this.clientId = clientId;
        this.penalty = penalty;
        this.noShow = noShow;
        this.reservationId = reservationId;
        this.response = response;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }


    public boolean isPenalty() {
        return penalty;
    }

    public void setPenalty(boolean penalty) {
        this.penalty = penalty;
    }

    public boolean isNoShow() {
        return noShow;
    }

    public void setNoShow(boolean noShow) {
        this.noShow = noShow;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
