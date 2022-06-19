package com.project.team9.dto;


public class ClientReviewResponseDTO {
    private Long requestId;
    private String response;
    private Long clientId;
    private String text;
    private int rating;
    private Long resourceId;
    private Long vendorId;

    public ClientReviewResponseDTO() {
    }

    public ClientReviewResponseDTO(Long requestId, String response, Long clientId, String text, int rating, Long resourceId, Long vendorId) {
        this.requestId = requestId;
        this.response = response;
        this.clientId = clientId;
        this.text = text;
        this.rating = rating;
        this.resourceId = resourceId;
        this.vendorId = vendorId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
}
