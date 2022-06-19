package com.project.team9.dto;

import com.project.team9.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

public class VacationHouseQuickReservationDTO {
    private Long reservationID;
    private String startDate;
    private int numberOfPeople;
    private List<Tag> additionalServices;
    private int duration;
    private int price;
    private int discount;
    private List<String> tagsText;
    private Long clientID;

    public VacationHouseQuickReservationDTO(){}

    public VacationHouseQuickReservationDTO(Long reservationID, Long clientID, String startDate, int numberOfPeople, List<Tag> additionalServices, int duration, int price, int discount) {
        this.reservationID = reservationID;
        this.startDate = startDate;
        this.numberOfPeople = numberOfPeople;
        this.additionalServices = additionalServices;
        this.duration = duration;
        this.price = price;
        this.discount = discount;
        this.clientID = clientID;
    }
    public VacationHouseQuickReservationDTO(Long reservationID, String startDate, int numberOfPeople, List<Tag> additionalServices, int duration, int price, int discount) {
        this.reservationID = reservationID;
        this.startDate = startDate;
        this.numberOfPeople = numberOfPeople;
        this.additionalServices = additionalServices;
        this.duration = duration;
        this.price = price;
        this.discount = discount;
        this.clientID = null;
    }

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    public List<String> getTagsText() {
        return tagsText;
    }
    public void setTagsText(List<String> tagsText) {
        this.tagsText = tagsText;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public List<Tag> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<Tag> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "VacationHouseQuickReservationDTO{" +
                "reservationID=" + reservationID +
                ", startDate='" + startDate + '\'' +
                ", numberOfPeople=" + numberOfPeople +
                ", additionalServices=" + additionalServices +
                ", duration=" + duration +
                ", price=" + price +
                ", discount=" + discount +
                ", tagsText=" + tagsText +
                ", clientID=" + clientID +
                '}';
    }
}
