package com.project.team9.model;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @SequenceGenerator(
            name = "address_sequence",
            sequenceName = "address_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_sequence"
    )
    private Long id;

    private String place;
    private String number;
    private String street;
    private String country;

    public Address(String location) {
        String[] tokens=location.split(",");
        this.place=tokens[1].trim();
        this.country=tokens[2].trim();
        tokens=tokens[0].split(" ");
        this.number=tokens[tokens.length-1].trim();
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i <tokens.length-1; i++) {
            stringBuilder.append(tokens[i]);
        }
        this.street=stringBuilder.toString().trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address() {
    }

    public Address(String place, String number, String street, String country) {
        this.place = place;
        this.number = number;
        this.street = street;
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "place='" + place + '\'' +
                ", number='" + number + '\'' +
                ", street='" + street + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public String getFullAddressName() {
        return this.street+" "+this.number+","+this.place+","+this.country;
    }


}
