package com.project.team9.dto;

public class ClientReviewDTO {
    private Long id;
    private Long clientId;
    private String profilePicture;
    private String name;
    private int rating;
    private String comment;

    public ClientReviewDTO() {}

    public ClientReviewDTO(Long id, String profilePicture, String name, int rating, String comment, Long clientId) {
        this.id = id;
        this.clientId = clientId;
        this.profilePicture = profilePicture;
        this.name = name;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClientReviewDTO{" +
                "id=" + id +
                ", profilePicture='" + profilePicture + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
