package com.project.team9.dto;

public class ReviewScoresDTO {
    private double fiveStars;
    private double fourStars;
    private double threeStars;
    private double twoStars;
    private double oneStars;

    public ReviewScoresDTO() {
    }

    public ReviewScoresDTO(double fiveStars, double fourStars, double threeStars, double twoStars, double oneStars) {
        this.fiveStars = fiveStars;
        this.fourStars = fourStars;
        this.threeStars = threeStars;
        this.twoStars = twoStars;
        this.oneStars = oneStars;
    }

    public double getThreeStars() {
        return threeStars;
    }

    public void setThreeStars(double threeStars) {
        this.threeStars = threeStars;
    }

    public double getTwoStars() {
        return twoStars;
    }

    public void setTwoStars(double twoStars) {
        this.twoStars = twoStars;
    }

    public double getOneStars() {
        return oneStars;
    }

    public void setOneStars(double oneStars) {
        this.oneStars = oneStars;
    }

    public double getFiveStars() {
        return fiveStars;
    }

    public void setFiveStars(double fiveStars) {
        this.fiveStars = fiveStars;
    }

    public double getFourStars() {
        return fourStars;
    }

    public void setFourStars(double fourStars) {
        this.fourStars = fourStars;
    }
}
