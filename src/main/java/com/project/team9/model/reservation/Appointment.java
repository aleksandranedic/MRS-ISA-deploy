package com.project.team9.model.reservation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Month;

@Entity
public class Appointment {
    @Id
    @SequenceGenerator(
            name = "appointment_sequence",
            sequenceName = "appointment_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appointment_sequence"
    )
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Appointment() {
    }

    public Appointment(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Appointment getDayAppointment(int year, int month, int day) {
        return new Appointment(LocalDateTime.of(year, Month.of(month), day, 10, 0, 0), LocalDateTime.of(year, Month.of(month), day, 10, 0, 0).plusDays(1));
    }

    public static Appointment getHourAppointment(int year, int month, int day, int hour, int minute) {
        return new Appointment(LocalDateTime.of(year, Month.of(month), day, hour, minute,0), LocalDateTime.of(year, Month.of(month), day, hour+1, minute,0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
