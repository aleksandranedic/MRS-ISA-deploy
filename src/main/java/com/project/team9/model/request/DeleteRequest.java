package com.project.team9.model.request;

import javax.persistence.Entity;
import javax.persistence.Version;

@Entity
public class DeleteRequest extends Request{
    private Long userId;
    private String userType;
    @Version
    private Long version;

    public DeleteRequest() {
    }

    public DeleteRequest(String text, String response, Long userId, String userType) {
        super(text, response);
        this.userId = userId;
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
