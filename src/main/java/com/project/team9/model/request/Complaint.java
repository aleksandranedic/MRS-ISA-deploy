package com.project.team9.model.request;

import javax.persistence.Entity;
import javax.persistence.Version;

@Entity
public class Complaint extends Request {
    private Long userId;
    private String entityType; //ADVENTURE,BOAT,VACATION_HOUSE,FISHING_INSTRUCTOR,BOAT_OWNER,VACATION_HOUSE_OWNER
    private Long entityId;
    @Version
    private Long version;

    public Complaint() {
    }

    public Complaint(String text, String response, Long userId, Long entityId,String entityType) {
        super(text, response);
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
