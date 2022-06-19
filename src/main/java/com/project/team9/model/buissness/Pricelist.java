package com.project.team9.model.buissness;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Pricelist {
    @Id
    @SequenceGenerator(
            name = "pricelist_sequence",
            sequenceName = "pricelist_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pricelist_sequence"
    )
    private Long id;
    private int price;
    private Date startTime;
    private Date endTime;


    public Pricelist() {

    }

    public Pricelist(int price, Date startTime) {
        this.price = price;
        this.startTime = startTime;
    }

    public Pricelist(int price, Date startTime, Date endTime) {
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
