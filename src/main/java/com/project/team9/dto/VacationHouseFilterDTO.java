package com.project.team9.dto;

import java.util.ArrayList;

public class VacationHouseFilterDTO {
    private boolean vacationHousesChecked;
    private String numOfVacationHouseRooms;
    private String numOfVacationHouseBeds;
    private String vacationHouseOwnerName;
    private ArrayList<Integer> priceRange;
    private String reviewRating;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;
    private boolean cancellationFee;

    public VacationHouseFilterDTO(boolean vacationHousesChecked, String numOfVacationHouseRooms, String numOfVacationHouseBeds, String vacationHouseOwnerName, ArrayList<Integer> priceRange, String reviewRating, String startDate, String endDate, String startTime, String endTime, String location, boolean cancellationFee) {
        this.vacationHousesChecked = vacationHousesChecked;
        this.numOfVacationHouseRooms = numOfVacationHouseRooms;
        this.numOfVacationHouseBeds = numOfVacationHouseBeds;
        this.vacationHouseOwnerName = vacationHouseOwnerName;
        this.priceRange = priceRange;
        this.reviewRating = reviewRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.cancellationFee = cancellationFee;
    }

    public VacationHouseFilterDTO() {
    }

    public boolean isVacationHousesChecked() {
        return vacationHousesChecked;
    }

    public void setVacationHousesChecked(boolean vacationHousesChecked) {
        this.vacationHousesChecked = vacationHousesChecked;
    }

    public String getNumOfVacationHouseRooms() {
        return numOfVacationHouseRooms;
    }

    public void setNumOfVacationHouseRooms(String numOfVacationHouseRooms) {
        this.numOfVacationHouseRooms = numOfVacationHouseRooms;
    }

    public String getNumOfVacationHouseBeds() {
        return numOfVacationHouseBeds;
    }

    public void setNumOfVacationHouseBeds(String numOfVacationHouseBeds) {
        this.numOfVacationHouseBeds = numOfVacationHouseBeds;
    }

    public String getVacationHouseOwnerName() {
        return vacationHouseOwnerName;
    }

    public void setVacationHouseOwnerName(String vacationHouseOwnerName) {
        this.vacationHouseOwnerName = vacationHouseOwnerName;
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
