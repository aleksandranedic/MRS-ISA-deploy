package com.project.team9.dto;


public class ClientReviewRequestDTO {
    private String resourceTitle;
    private String vendorFullName;
    private int rating;
    private String clientFullName;
    private String comment;
    private Long resourceId;
    private Long clientId;
    private Long vendorId;
    private Long requestId;

    public ClientReviewRequestDTO() {
    }

    public ClientReviewRequestDTO(String resourceTitle, String vendorFullName, int rating, String clientFullName, String comment, Long resourceId, Long clientId, Long vendorId, Long requestId) {
        this.resourceTitle = resourceTitle;
        this.vendorFullName = vendorFullName;
        this.rating = rating;
        this.clientFullName = clientFullName;
        this.comment = comment;
        this.resourceId = resourceId;
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.requestId = requestId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
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

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
