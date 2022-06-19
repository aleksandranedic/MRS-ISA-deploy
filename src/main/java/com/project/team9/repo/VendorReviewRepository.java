package com.project.team9.repo;

import com.project.team9.model.request.VendorReviewRequest;
import com.project.team9.model.review.VendorReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorReviewRepository extends JpaRepository<VendorReview, Long> {

    @Query("FROM VendorReview WHERE reservationId = ?1")
    List<VendorReview> findReviewForReservation(Long id);
}
