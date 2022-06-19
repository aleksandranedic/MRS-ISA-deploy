package com.project.team9.dto;

public class VerificationDTO {
    private String messageTitle;
    private String message;
    private boolean confirmed;

    public VerificationDTO() {
    }

    public VerificationDTO(String messageTitle, String message, boolean confirmed) {
        this.messageTitle = messageTitle;
        this.message = message;
        this.confirmed = confirmed;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
