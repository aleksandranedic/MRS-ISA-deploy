package com.project.team9.dto;

import java.util.List;

public class NewReservationDTO {
    private Long clientId;
    private Long resourceId;
    private int numberOfClients;
    private List<String> additionalServicesStrings;
    private int price;
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMinute;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMinute;

    private String type;
    private boolean isBusyPeriod;
    private boolean isQuickReservation;

    public NewReservationDTO() {
    }

    public NewReservationDTO(Long clientId, Long resourceId, int numberOfClients, List<String> additionalServicesStrings, int price, int startYear, int startMonth, int startDay, int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute, String type, boolean isBusyPeriod, boolean isQuickReservation) {
        this.clientId = clientId;
        this.resourceId = resourceId;
        this.numberOfClients = numberOfClients;
        this.additionalServicesStrings = additionalServicesStrings;
        this.price = price;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.type = type;
        this.isBusyPeriod = isBusyPeriod;
        this.isQuickReservation = isQuickReservation;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public List<String> getAdditionalServicesStrings() {
        return additionalServicesStrings;
    }

    public void setAdditionalServicesStrings(List<String> additionalServicesStrings) {
        this.additionalServicesStrings = additionalServicesStrings;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isBusyPeriod() {
        return isBusyPeriod;
    }

    public void setBusyPeriod(boolean busyPeriod) {
        isBusyPeriod = busyPeriod;
    }

    public boolean isQuickReservation() {
        return isQuickReservation;
    }

    public void setQuickReservation(boolean quickReservation) {
        isQuickReservation = quickReservation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
