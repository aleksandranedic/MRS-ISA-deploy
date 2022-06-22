package com.project.team9.dto;

public class VendorReviewResponseDTO {
    private String response;
    private Long clientId;
    private Long vendorReviewRequestId;
    private boolean penalty;
    private boolean noShow;
    private Long reservationId;
    private Long resourceId;
    private int rating;
    private String text;
    private boolean checkNoShow;
    private boolean checkPenalty;

    public VendorReviewResponseDTO() {
    }

    public VendorReviewResponseDTO(String response, Long clientId, Long vendorReviewRequestId, boolean penalty, boolean noShow, Long reservationId, Long resourceId, int rating, String text, boolean checkNoShow, boolean checkPenalty) {
        this.response = response;
        this.clientId = clientId;
        this.vendorReviewRequestId = vendorReviewRequestId;
        this.penalty = penalty;
        this.noShow = noShow;
        this.reservationId = reservationId;
        this.resourceId = resourceId;
        this.rating = rating;
        this.text = text;
        this.checkNoShow = checkNoShow;
        this.checkPenalty = checkPenalty;
    }

    public Long getVendorReviewRequestId() {
        return vendorReviewRequestId;
    }

    public void setVendorReviewRequestId(Long vendorReviewRequestId) {
        this.vendorReviewRequestId = vendorReviewRequestId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCheckNoShow() {
        return checkNoShow;
    }

    public void setCheckNoShow(boolean checkNoShow) {
        this.checkNoShow = checkNoShow;
    }

    public boolean isCheckPenalty() {
        return checkPenalty;
    }

    public void setCheckPenalty(boolean checkPenalty) {
        this.checkPenalty = checkPenalty;
    }
}
