package com.project.team9.dto;

public class SubscribeDTO {
    private Long userId;
    private Long entityId;

    public SubscribeDTO() {
    }

    public SubscribeDTO(Long userId, Long entityId) {
        this.userId = userId;
        this.entityId = entityId;
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
}
