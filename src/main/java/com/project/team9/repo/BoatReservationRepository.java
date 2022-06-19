package com.project.team9.repo;

import com.project.team9.model.reservation.BoatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BoatReservationRepository extends JpaRepository<BoatReservation, Long> {

    @Query("FROM BoatReservation WHERE isQuickReservation = false AND isBusyPeriod = false AND deleted= false")
    List<BoatReservation> findStandardReservations();

    @Query("FROM BoatReservation  WHERE resource.id = ?1 AND deleted = false")
    List<BoatReservation> findPossibleCollisionReservations(Long resourceId);

    @Query("FROM BoatReservation  WHERE isBusyPeriod = true AND resource.id = ?1 AND deleted= false")
    List<BoatReservation> findBusyPeriodForBoat(Long id);

    @Query("FROM BoatReservation  WHERE resource.id = ?1 AND client.id = ?2 AND deleted= false")
    List<BoatReservation> findReservationsForClient(Long resourceId, Long clientId);

    @Query("FROM BoatReservation  WHERE resource.owner.id = ?1 AND client.id = ?2 AND deleted = false")
    List<BoatReservation> findReservationsForClientAndVendor(Long vendorId, Long clientId);

    @Query("FROM BoatReservation WHERE resource.id=?1 AND deleted = false")
    List<BoatReservation> findReservationsByResourceId(Long id);

    @Query("FROM BoatReservation WHERE resource.owner.id=?1 AND deleted = false")
    List<BoatReservation> findReservationsByVendorId(Long id);

    @Query("FROM BoatReservation WHERE client.id=?1 AND deleted= false")
    List<BoatReservation> findBoatReservationByClientId(Long id);
}
