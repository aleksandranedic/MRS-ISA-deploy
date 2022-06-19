package com.project.team9.model.request;

import com.project.team9.model.review.ClientReview;
import com.project.team9.model.review.VendorReview;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ClientReviewRequest extends Request{

    Long resourceId;
    Long vendorId;
    int rating;
    long clientId;

    public ClientReviewRequest() {
    }

    public ClientReviewRequest(String comment, String response, Long resourceId, Long vendorId, int rating, long clientId) {
        super(comment, response);
        this.resourceId = resourceId;
        this.vendorId = vendorId;
        this.rating = rating;
        this.clientId = clientId;
    }

    public ClientReviewRequest(ClientReview review) {
        super(review.getText(), "");
        this.resourceId = review.getResourceId();
        this.vendorId = review.getVendorId();
        this.rating = review.getRating();
        this.clientId = review.getClientId();
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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
