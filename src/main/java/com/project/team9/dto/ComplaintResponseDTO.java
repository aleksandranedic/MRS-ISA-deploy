package com.project.team9.dto;

public class ComplaintResponseDTO {
    private Long complaintId;
    private Long userId;
    private Long entityId;
    private String response;
    private String entityType;

    public ComplaintResponseDTO() {
    }

    public ComplaintResponseDTO(Long complaintId, Long userId, Long entityId, String response, String entityType) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.entityId = entityId;
        this.response = response;
        this.entityType = entityType;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
