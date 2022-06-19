package com.project.team9.dto;

public class ComplaintDTO {
    private Long userId;
    private String entityType; //ADVENTURE,BOAT,VACATION_HOUSE,FISHING_INSTRUCTOR,BOAT_OWNER,VACATION_HOUSE_OWNER
    private Long entityId;
    private String entityName;
    private String userFullName;
    private String comment;
    private Long complaintId;

    public ComplaintDTO() {
    }

    public ComplaintDTO(Long userId, String entityType, Long entityId, String entityName, String userFullName, String comment, Long complaintId) {
        this.userId = userId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
        this.userFullName = userFullName;
        this.comment = comment;
        this.complaintId = complaintId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
