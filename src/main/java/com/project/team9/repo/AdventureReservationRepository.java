package com.project.team9.repo;

import com.project.team9.model.reservation.AdventureReservation;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdventureReservationRepository extends JpaRepository<AdventureReservation, Long> {

    @Query("FROM AdventureReservation WHERE isBusyPeriod = false AND deleted= false")
    List<AdventureReservation> findStandardReservations();

    @Query("FROM AdventureReservation WHERE isBusyPeriod = true AND deleted= false")
    List<AdventureReservation> findBusyPeriods();

    @Query("FROM AdventureReservation WHERE (resource.id = ?1 OR resource.owner.id = ?2 ) AND deleted= false")
    List<AdventureReservation> findPossibleCollisionReservations(Long resourceId, Long ownerId);

    @Query("FROM AdventureReservation WHERE isBusyPeriod = true AND resource.owner.id = ?1 AND deleted= false")
    List<AdventureReservation> findBusyPeriodsForFishingInstructor(Long id);

    @Query("FROM AdventureReservation WHERE isBusyPeriod = true AND (resource.id = ?1 OR resource.owner.id = ?2) AND deleted= false")
    List<AdventureReservation> findBusyPeriodsForAdventure(Long id, Long ownerId);

    @Query("FROM AdventureReservation WHERE resource.id=?1 AND client.id = ?2 AND deleted= false")
    List<AdventureReservation> findReservationsForClient(Long resourceId, Long clientId);

    @Query("FROM AdventureReservation WHERE resource.id=?1 AND deleted = false")
    List<AdventureReservation> findReservationsByResourceId(Long resourceId);

    @Query("FROM AdventureReservation WHERE resource.owner.id=?1 AND client.id = ?2 AND deleted= false" )
    List<AdventureReservation> findReservationsForVendorAndClient(Long vendorId, Long clientId);

    @Query("FROM AdventureReservation WHERE resource.owner.id=?1 AND deleted= false")
    List<AdventureReservation> findReservationsForVendor(Long vendorId);

    @Query("FROM AdventureReservation WHERE client.id=?1 AND deleted= false")
    List<AdventureReservation> findAdventureReservationForClientId(Long id);

    @Query("FROM AdventureReservation  WHERE deleted = false")
    List<AdventureReservation> getReservations();

    @Query("FROM AdventureReservation  WHERE (client.id = ?1 AND deleted = false) OR (client.id = ?1 AND resource.id = ?1 AND deleted=true) ")
    List<AdventureReservation> getPossibleCollisionReservationsForClient(Long clientId, Long resourceId);
}
