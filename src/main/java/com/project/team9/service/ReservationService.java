package com.project.team9.service;

import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.reservation.Appointment;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    public void checkAppointmentCollision(Appointment existingAppointment, Appointment newAppointment) throws ReservationNotAvailableException{
        if (newAppointment.getStartTime().isAfter(existingAppointment.getStartTime()) && newAppointment.getStartTime().isBefore(existingAppointment.getEndTime())) {
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getEndTime().isAfter(existingAppointment.getStartTime()) && newAppointment.getEndTime().isBefore(existingAppointment.getEndTime())) {
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getStartTime().isBefore(existingAppointment.getStartTime()) && newAppointment.getEndTime().isAfter(existingAppointment.getEndTime())) {
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getStartTime().isAfter(existingAppointment.getStartTime()) && newAppointment.getEndTime().isBefore(existingAppointment.getEndTime())) {
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getStartTime().isEqual(existingAppointment.getStartTime())){
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getEndTime().isEqual(existingAppointment.getStartTime())){
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getStartTime().isEqual(existingAppointment.getEndTime())){
            throw new ReservationNotAvailableException("Reservation not available");
        }
        if (newAppointment.getEndTime().isEqual(existingAppointment.getEndTime())){
            throw new ReservationNotAvailableException("Reservation not available");
        }

    }


}
