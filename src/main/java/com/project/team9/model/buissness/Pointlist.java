package com.project.team9.model.buissness;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Pointlist {
    @Id
    @SequenceGenerator(
            name = "pointlist_sequence",
            sequenceName = "pointlist_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pointlist_sequence"
    )
    private Long id;
    private int numOfPoints;
    private Date startTime;
    private Date endTime;
    private String type; // CLIENT, VENDOR

    public Pointlist() {
    }

    public Pointlist(int numOfPoints, String type) {
        this.numOfPoints = numOfPoints;
        this.startTime = new Date();
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumOfPoints() {
        return numOfPoints;
    }

    public void setNumOfPoints(int numOfPoints) {
        this.numOfPoints = numOfPoints;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
