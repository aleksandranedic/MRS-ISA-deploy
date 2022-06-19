package com.project.team9.model.request;

import com.project.team9.model.review.VendorReview;

import javax.persistence.Entity;

@Entity
public class VendorReviewRequest extends Request{
    Long resourceId;
    Long vendorId;
    int rating;
    Long clientId;
    boolean penalty;
    boolean noShow;
    private Long reservationId;

    public VendorReviewRequest() {
    }

    public VendorReviewRequest(String comment, String response, Long resourceId, Long vendorId, int rating, Long clientId, boolean penalty, boolean noShow, Long reservationId) {
        super(comment, response);
        this.resourceId = resourceId;
        this.vendorId = vendorId;
        this.rating = rating;
        this.clientId = clientId;
        this.penalty = penalty;
        this.noShow = noShow;
        this.reservationId = reservationId;
    }

    public VendorReviewRequest(VendorReview vendorReview) {
        super(vendorReview.getText(), "");
        this.resourceId = vendorReview.getResourceId();
        this.vendorId = vendorReview.getVendorId();
        this.rating = vendorReview.getRating();
        this.clientId = vendorReview.getClientId();
        this.penalty = vendorReview.isPenalty();
        this.noShow = vendorReview.isNoShow();
        this.reservationId = vendorReview.getReservationId();

    }


    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
}
