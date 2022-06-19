package com.project.team9.dto;


public class DeleteReplayDTO {
    private String username;
    private String comment;
    private String requestId;
    private String type;

    public DeleteReplayDTO() {
    }

    public DeleteReplayDTO(String username, String comment, String requestId, String type) {
        this.username = username;
        this.comment = comment;
        this.requestId = requestId;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
