package com.project.team9.service;

import com.project.team9.model.reservation.Appointment;
import com.project.team9.repo.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public void save(Appointment appointment) {
        repository.save(appointment);
    }

    public void saveAll(List<Appointment> appointments) {
        repository.saveAll(appointments);
    }

    public void delete(Appointment appointment) {
        repository.delete(appointment);
    }
}
