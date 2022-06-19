package com.project.team9.dto;

public class VendorRequestReviewDenialDTO {
    private Long vendorReviewRequestId;
    private String response;
    private Long resourceId;
    public VendorRequestReviewDenialDTO() {
    }

    public VendorRequestReviewDenialDTO(Long vendorReviewRequestId, String response, Long resourceId) {
        this.vendorReviewRequestId = vendorReviewRequestId;
        this.response = response;
        this.resourceId = resourceId;
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

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
