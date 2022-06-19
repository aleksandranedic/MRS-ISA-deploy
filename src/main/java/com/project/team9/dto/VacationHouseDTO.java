package com.project.team9.dto;

import com.project.team9.model.Tag;
import com.project.team9.model.reservation.VacationHouseReservation;

import java.util.List;

public class VacationHouseDTO {
    private Long id;
    private String name;
    private String address;
    private String number;
    private String street;
    private String city;
    private String country;
    private String description;
    private List<String> imagePaths;
    private String rulesAndRegulations;
    private List<Tag> additionalServices;
    private List<String> tagsText;
    private int price;
    private int cancellationFee;
    private int numberOfRooms;
    private int capacity;
    private Long ownerId;
    private List<VacationHouseQuickReservationDTO> quickReservations;

    public VacationHouseDTO() {}

    public VacationHouseDTO(Long id, String name,String address, String number, String street, String city, String country, String description, List<String> imagePaths, String rulesAndRegulations, List<Tag> additionalServices, int price, int cancellationFee, int numberOfRooms, int capacity, List<VacationHouseQuickReservationDTO> quickReservations) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.street = street;
        this.city = city;
        this.country = country;
        this.description = description;
        this.imagePaths = imagePaths;
        this.rulesAndRegulations = rulesAndRegulations;
        this.additionalServices = additionalServices;
        this.price = price;
        this.cancellationFee = cancellationFee;
        this.numberOfRooms = numberOfRooms;
        this.capacity = capacity;
        this.quickReservations = quickReservations;
    }

    public VacationHouseDTO(Long id, String name,String address, String number, String street, String city, String country, String description, List<String> imagePaths, String rulesAndRegulations, List<Tag> additionalServices, List<String> tagsText, int price, int cancellationFee, int numberOfRooms, int capacity, List<VacationHouseQuickReservationDTO> quickReservations) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.street = street;
        this.city = city;
        this.country = country;
        this.description = description;
        this.imagePaths = imagePaths;
        this.rulesAndRegulations = rulesAndRegulations;
        this.additionalServices = additionalServices;
        this.tagsText = tagsText;
        this.price = price;
        this.cancellationFee = cancellationFee;
        this.numberOfRooms = numberOfRooms;
        this.capacity = capacity;
        this.quickReservations = quickReservations;
    }


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getTagsText() {
        return tagsText;
    }

    public void setTagsText(List<String> tagsText) {
        this.tagsText = tagsText;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getRulesAndRegulations() {
        return rulesAndRegulations;
    }

    public void setRulesAndRegulations(String rulesAndRegulations) {
        this.rulesAndRegulations = rulesAndRegulations;
    }

    public List<Tag> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<Tag> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(int cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<VacationHouseQuickReservationDTO> getQuickReservations() {
        return quickReservations;
    }

    public void setQuickReservations(List<VacationHouseQuickReservationDTO> quickReservations) {
        this.quickReservations = quickReservations;
    }

    @Override
    public String toString() {
        return "VacationHouseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", number='" + number + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", imagePaths=" + imagePaths +
                ", rulesAndRegulations='" + rulesAndRegulations + '\'' +
                ", additionalServices=" + additionalServices +
                ", tagsText=" + tagsText +
                ", price=" + price +
                ", cancellationFee=" + cancellationFee +
                ", numberOfRooms=" + numberOfRooms +
                ", capacity=" + capacity +
                ", quickReservations=" + quickReservations +
                '}';
    }
}
