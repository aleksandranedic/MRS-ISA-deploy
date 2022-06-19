package com.project.team9.repo;

import com.project.team9.model.request.VendorReviewRequest;
import com.project.team9.model.reservation.VacationHouseReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VendorReviewRequestRepository extends JpaRepository<VendorReviewRequest, Long> {

    @Query("FROM VendorReviewRequest WHERE reservationId = ?1")
    List<VendorReviewRequest> findReviewForReservation(Long id);
}
