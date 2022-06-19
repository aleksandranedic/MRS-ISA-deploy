package com.project.team9.service;

import com.project.team9.model.reservation.AdventureReservation;
import com.project.team9.model.reservation.BoatReservation;
import com.project.team9.repo.BoatReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoatReservationService {
    private final BoatReservationRepository repository;

    @Autowired
    public BoatReservationService(BoatReservationRepository repository) {
        this.repository = repository;
    }

    public void addReservation(BoatReservation reservation) {
        this.repository.save(reservation);
    }
    public BoatReservation getBoatReservation(Long id) {
        return repository.getById(id);
    }
    public void deleteById(Long id) {
        BoatReservation boatReservation=repository.getById(id);
        boatReservation.setDeleted(true);
        repository.save(boatReservation);
    }
    public List<BoatReservation> getAll() {
        return repository.findAll().stream().filter(boatReservation -> !boatReservation.isDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<BoatReservation> getStandardReservations() {
        return repository.findStandardReservations();
    }

    public List<BoatReservation> getPossibleCollisionReservations(Long id){
        return repository.findPossibleCollisionReservations(id);
    }

    public Long save(BoatReservation reservation) {
        repository.save(reservation);
        return null;
    }

    @Transactional(readOnly = false)
    public Long saveQuickReservationAsReservation(BoatReservation reservation) {
        return repository.save(reservation).getId();
    }

    public List<BoatReservation> getBusyPeriodForBoat(Long id) {
        return repository.findBusyPeriodForBoat(id);
    }

    public boolean hasReservations(Long resourceId, Long clientId) {
        return repository.findReservationsForClient(resourceId, clientId).size() > 0;
    }


    public boolean clientCanReviewVendor(Long vendorId, Long clientId) {
        return repository.findReservationsForClientAndVendor(vendorId, clientId).size()>0;
    }

    public List<BoatReservation> getReservationsByBoatId(Long id) {
        return repository.findReservationsByResourceId(id);
    }

    public List<BoatReservation> getBoatReservationsForVendorId(Long id) {
        return repository.findReservationsByVendorId(id);
    }

    public List<BoatReservation> getBoatReservationsForClientId(Long id) {
        return repository.findBoatReservationByClientId(id);
    }

    public void deleteReservation(BoatReservation boatReservation) {
        boatReservation.setDeleted(true);
        repository.save(boatReservation);
    }
}
