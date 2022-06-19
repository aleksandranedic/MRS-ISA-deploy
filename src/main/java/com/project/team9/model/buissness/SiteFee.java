package com.project.team9.model.buissness;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SiteFee {
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
    private int percentage;
    private Date startTime;
    private Date endTime;

    public SiteFee() {
    }

    public SiteFee(int percentage, Date startTime) {
        this.percentage = percentage;
        this.startTime = startTime;
    }

    public SiteFee(int percentage) {
        this.percentage = percentage;
        this.startTime = new Date();
    }

    public SiteFee(int percentage, Date startTime, Date endTime) {
        this.percentage = percentage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }


    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int price) {
        this.percentage = price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
