package com.project.team9.dto;

import com.project.team9.model.buissness.UserCategory;

public class UserStatDTO {
    private int penalty;
    private int points;
    private UserCategory category;
    private double rating;

    public UserStatDTO() {
    }

    public UserStatDTO(int penalty, int points, UserCategory category, double rating) {
        this.penalty = penalty;
        this.points = points;
        this.category = category;
        this.rating = rating;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
