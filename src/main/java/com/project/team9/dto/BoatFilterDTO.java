package com.project.team9.dto;

import java.util.ArrayList;

public class BoatFilterDTO {
    private boolean boatsChecked;
    private String boatType;
    private String boatEnginePower;
    private String boatEngineNum;
    private String boatMaxSpeed;
    private String boatCapacity;
    private String boatOwnerName;
    private ArrayList<Integer> priceRange;
    private String reviewRating;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;
    private boolean cancellationFee;

    public BoatFilterDTO() {
    }

    public BoatFilterDTO(boolean boatsChecked, String boatType, String boatEnginePower, String boatEngineNum, String boatMaxSpeed, String boatCapacity, String boatOwnerName, ArrayList<Integer> priceRange, String reviewRating, String startDate, String endDate, String startTime, String endTime, String location, boolean cancellationFee) {
        this.boatsChecked = boatsChecked;
        this.boatType = boatType;
        this.boatEnginePower = boatEnginePower;
        this.boatEngineNum = boatEngineNum;
        this.boatMaxSpeed = boatMaxSpeed;
        this.boatCapacity = boatCapacity;
        this.boatOwnerName = boatOwnerName;
        this.priceRange = priceRange;
        this.reviewRating = reviewRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.cancellationFee = cancellationFee;
    }

    public boolean isBoatsChecked() {
        return boatsChecked;
    }

    public void setBoatsChecked(boolean boatsChecked) {
        this.boatsChecked = boatsChecked;
    }

    public String getBoatType() {
        return boatType;
    }

    public void setBoatType(String boatType) {
        this.boatType = boatType;
    }

    public String getBoatEnginePower() {
        return boatEnginePower;
    }

    public void setBoatEnginePower(String boatEnginePower) {
        this.boatEnginePower = boatEnginePower;
    }

    public String getBoatEngineNum() {
        return boatEngineNum;
    }

    public void setBoatEngineNum(String boatEngineNum) {
        this.boatEngineNum = boatEngineNum;
    }

    public String getBoatMaxSpeed() {
        return boatMaxSpeed;
    }

    public void setBoatMaxSpeed(String boatMaxSpeed) {
        this.boatMaxSpeed = boatMaxSpeed;
    }

    public String getBoatCapacity() {
        return boatCapacity;
    }

    public void setBoatCapacity(String boatCapacity) {
        this.boatCapacity = boatCapacity;
    }

    public String getBoatOwnerName() {
        return boatOwnerName;
    }

    public void setBoatOwnerName(String boatOwnerName) {
        this.boatOwnerName = boatOwnerName;
    }

    public ArrayList<Integer> getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(ArrayList<Integer> priceRange) {
        this.priceRange = priceRange;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(boolean cancellationFee) {
        this.cancellationFee = cancellationFee;
    }
}
