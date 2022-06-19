package com.project.team9.service;

import com.project.team9.model.reservation.VacationHouseReservation;
import com.project.team9.repo.VacationHouseReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class VacationHouseReservationService {
    private final VacationHouseReservationRepository repository;

    @Autowired
    public VacationHouseReservationService(VacationHouseReservationRepository repository) {
        this.repository = repository;
    }

    public void addReservation(VacationHouseReservation reservation) {
        this.repository.save(reservation);
    }
    public VacationHouseReservation getVacationHouseReservation(Long id) {
        return repository.getById(id);
    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<VacationHouseReservation> getAll() {
        return repository.findAll();
    }

    public List<VacationHouseReservation> getStandardReservations() {
        return repository.findStandardReservations();
    }

    public List<VacationHouseReservation> getPossibleCollisionReservations(Long id) {
        return repository.findPossibleCollisionReservations(id);
    }

    public Long save(VacationHouseReservation reservation) {
        return repository.save(reservation).getId();
    }

    @Transactional(readOnly = false)
    public Long saveQuickReservationAsReservation(VacationHouseReservation reservation) {
        return repository.save(reservation).getId();
    }

    public List<VacationHouseReservation> getBusyPeriodForVacationHouse(Long id) {
        return repository.findBusyPeriodForVacationHouse(id);
    }
    public boolean clientHasReservations(Long resourceId, Long clientId) {
        return repository.findReservationsForClient(resourceId, clientId).size() > 0;
    }

    public List<VacationHouseReservation> getReservationsByVacationHouseId(Long id) {
        return repository.findReservationsByResourceId(id);
    }

    public boolean clientCanReviewVendor(Long vendorId, Long clientId) {
        return repository.findReservationsForClientAndVendor(vendorId, clientId).size() > 0;
    }

    public List<VacationHouseReservation> getVacationHouseReservationsForVendorId(Long id) {
        return repository.findVacationHouseReservationForVendorId(id);
    }

    public List<VacationHouseReservation> getVacationHouseReservationsForClienId(Long id) {
        return repository.findVacationHouseReservationForClientId(id);
    }

    public void deleteReservation(VacationHouseReservation vacationHouseReservation) {
        vacationHouseReservation.setDeleted(true);
        repository.save(vacationHouseReservation);
    }
}
