package com.project.team9.dto;

import com.project.team9.model.Image;

public class ResourceReportDTO {
    private Long id;
    private String name;
    private Image image;
    private double rating;

    public ResourceReportDTO() {
    }

    public ResourceReportDTO(Long id, String name, Image image, double rating) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
