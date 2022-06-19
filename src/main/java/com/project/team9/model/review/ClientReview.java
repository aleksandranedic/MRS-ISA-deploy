package com.project.team9.model.review;

import javax.persistence.Entity;

@Entity
public class ClientReview extends Review{

    private long clientId;

    public ClientReview() {
    }

    public ClientReview(Long resourceId, Long vendorId, int rating, String text, long clientId,String response) {
        super(resourceId, vendorId, rating, text, response);
        this.clientId = clientId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
