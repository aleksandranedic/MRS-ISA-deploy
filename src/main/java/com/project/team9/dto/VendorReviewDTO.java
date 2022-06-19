package com.project.team9.dto;

public class VendorReviewDTO {
    private Long clientId;
    private Long vendorId;
    private Long reservationId;
    private Long resourceId;
    private String resourceTitle;
    private String vendorFullName;
    private int rating;
    private String clientFullName;
    private boolean penalty;
    private boolean noShow;
    private String comment;
    private Long vendorReviewRequestId;

    public VendorReviewDTO() {
    }

    public VendorReviewDTO(Long clientId, Long vendorId, Long reservationId, Long resourceId, String resourceTitle, String vendorFullName, int rating, String clientFullName, boolean penalty, boolean noShow, String comment, Long vendorReviewRequestId) {
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.reservationId = reservationId;
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.vendorFullName = vendorFullName;
        this.rating = rating;
        this.clientFullName = clientFullName;
        this.penalty = penalty;
        this.noShow = noShow;
        this.comment = comment;
        this.vendorReviewRequestId = vendorReviewRequestId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getVendorReviewRequestId() {
        return vendorReviewRequestId;
    }

    public void setVendorReviewRequestId(Long vendorReviewRequestId) {
        this.vendorReviewRequestId = vendorReviewRequestId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getVendorFullName() {
        return vendorFullName;
    }

    public void setVendorFullName(String vendorFullName) {
        this.vendorFullName = vendorFullName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
