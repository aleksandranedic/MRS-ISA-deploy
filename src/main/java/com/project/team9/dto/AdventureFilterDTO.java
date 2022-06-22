package com.project.team9.dto;

import java.util.ArrayList;

public class AdventureFilterDTO {
    private boolean adventuresChecked;//true
    private String numberOfClients;
    private String fishingInstructorName;
    private ArrayList<Integer> priceRange; //100-200
    private String reviewRating;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;  //this.street+" "+this.number+","+this.place+","+this.country;
    private boolean cancellationFee;
    public AdventureFilterDTO() {
    }

    public AdventureFilterDTO(boolean adventuresChecked, String numberOfClients, String fishingInstructorName, ArrayList<Integer> priceRange, String reviewRating, String startDate, String endDate, String startTime, String endTime, String location, boolean cancellationFee) {
        this.adventuresChecked = adventuresChecked;
        this.numberOfClients = numberOfClients;
        this.fishingInstructorName = fishingInstructorName;
        this.priceRange = priceRange;
        this.reviewRating = reviewRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.cancellationFee = cancellationFee;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(boolean cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public boolean isAdventuresChecked() {
        return adventuresChecked;
    }

    public void setAdventuresChecked(boolean adventuresChecked) {
        this.adventuresChecked = adventuresChecked;
    }

    public String getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(String numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public String getFishingInstructorName() {
        return fishingInstructorName;
    }

    public void setFishingInstructorName(String fishingInstructorName) {
        this.fishingInstructorName = fishingInstructorName;
    }

    public ArrayList<Integer> getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(ArrayList<Integer> priceRange) {
        this.priceRange = priceRange;
    }
}
