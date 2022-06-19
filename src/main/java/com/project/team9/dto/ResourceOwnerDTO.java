package com.project.team9.dto;

import com.project.team9.model.Image;

public class ResourceOwnerDTO {
    private Long id;
    private String name;
    private Image profileImg;

    public ResourceOwnerDTO() {
    }

    public ResourceOwnerDTO(Long id, String name, Image image) {
        this.id = id;
        this.name = name;
        this.profileImg = image;
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

    public Image getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(Image profileImg) {
        this.profileImg = profileImg;
    }
}
