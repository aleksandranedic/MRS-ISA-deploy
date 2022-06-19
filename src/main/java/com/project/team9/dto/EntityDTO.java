package com.project.team9.dto;

import com.project.team9.model.Address;
import com.project.team9.model.Image;

public class EntityDTO {
    private String title;
    private String entityType;
    private Image image;
    private double rating;
    private Long entityId;
    private Address address;
    private double price;

    public EntityDTO() {
    }

    public EntityDTO(String title, String entityType, Image image, double rating, Long entityId, Address address, double price) {
        this.title = title;
        this.entityType = entityType;
        this.image = image;
        this.rating = rating;
        this.entityId = entityId;
        this.address = address;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
