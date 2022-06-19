package com.project.team9.dto;

public class HouseCardDTO {
    private Long id;
    private String thumbnailPath;
    private String title;
    private String description;
    private String address;

    public HouseCardDTO() {}
    public HouseCardDTO(Long id, String thumbnailPath, String title, String description, String address) {
        this.id = id;
        this.thumbnailPath = thumbnailPath;
        this.title = title;
        this.description = description;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
