package com.project.team9.dto;

import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.Tag;
import com.project.team9.model.resource.Adventure;

import java.util.ArrayList;
import java.util.List;

public class AdventureDTO {
    private Long id;
    private String title;
    private String number;
    private String street;
    private String place;
    private String country;
    private String description;
    private List<String> imagePaths;
    private String rulesAndRegulations;
    private List<String> additionalServicesText;
    private int price;
    private int cancellationFee;
    private Long ownerId;
    private int numberOfClients;
    private List<String> fishingEquipmentText;

    public AdventureDTO() {
    }

    public AdventureDTO(Long id, String title, String number, String street, String place, String country, String description, List<String> imagePaths, String rulesAndRegulations, List<String> additionalServicesText, int price, int cancellationFee, Long ownerId, int numberOfClients, List<String> fishingEquipmentText) {
        this.id = id;
        this.title = title;

        this.number = number;
        this.street = street;
        this.place = place;
        this.country = country;
        this.description = description;
        this.imagePaths = imagePaths;
        this.rulesAndRegulations = rulesAndRegulations;
        this.additionalServicesText = additionalServicesText;
        this.price = price;
        this.cancellationFee = cancellationFee;
        this.ownerId = ownerId;
        this.numberOfClients = numberOfClients;
        this.fishingEquipmentText = fishingEquipmentText;
    }

    public AdventureDTO(Adventure adventure) {
        this.id = adventure.getId();
        this.title = adventure.getTitle();
        this.number = adventure.getAddress().getNumber();
        this.street = adventure.getAddress().getStreet();
        this.place = adventure.getAddress().getPlace();
        this.country = adventure.getAddress().getCountry();
        this.description = adventure.getDescription();
        this.imagePaths = null;
        this.rulesAndRegulations = adventure.getRulesAndRegulations();
        this.additionalServicesText = new ArrayList<>();
        for (Tag t: adventure.getAdditionalServices()) {
            this.additionalServicesText.add(t.getText());
        }
        this.price = adventure.getPricelist().getPrice();
        this.cancellationFee = adventure.getCancellationFee();
        this.ownerId = adventure.getOwner().getId();
        this.numberOfClients = adventure.getNumberOfClients();
        this.fishingEquipmentText = new ArrayList<>();
        for (Tag t: adventure.getFishingEquipment()) {
            this.fishingEquipmentText.add(t.getText());
        }
        this.imagePaths = new ArrayList<String>();
        for (Image i: adventure.getImages()) {
            this.imagePaths.add(i.getPath());
        }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public List<String> getAdditionalServicesText() {
        return additionalServicesText;
    }

    public void setAdditionalServicesText(List<String> additionalServicesText) {
        this.additionalServicesText = additionalServicesText;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public List<String> getFishingEquipmentText() {
        return fishingEquipmentText;
    }

    public void setFishingEquipmentText(List<String> fishingEquipmentText) {
        this.fishingEquipmentText = fishingEquipmentText;
    }

    @Override
    public String toString() {
        return "AdventureDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", number='" + number + '\'' +
                ", street='" + street + '\'' +
                ", place='" + place + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", imagePaths=" + imagePaths +
                ", rulesAndRegulations='" + rulesAndRegulations + '\'' +
                ", additionalServicesText=" + additionalServicesText +
                ", price=" + price +
                ", cancellationFee=" + cancellationFee +
                ", ownerId=" + ownerId +
                ", numberOfClients=" + numberOfClients +
                ", fishingEquipmentText=" + fishingEquipmentText +
                '}';
    }
}
