package com.project.team9.model.request;

import javax.persistence.*;

@MappedSuperclass
public abstract class Request {
    @Id
    @SequenceGenerator(
            name="request_sequence",
            sequenceName = "request_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy= GenerationType.SEQUENCE,
            generator="request_sequence"
    )
    private Long id;
    private String comment;
    private String response;
    private Boolean deleted=false;

    public Request() {
    }

    public Request(String comment, String response) {
        this.comment = comment;
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String text) {
        this.comment = text;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
