package com.project.team9.repo;

import com.project.team9.model.review.ClientReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClientReviewRepository extends JpaRepository<ClientReview, Long> {
    List<ClientReview> findByResourceId(Long resource_id);
    List<ClientReview> findByVendorId(Long vendor_id);

    @Query("FROM ClientReview WHERE clientId = ?2 AND resourceId = ?1")
    List<ClientReview> findReviewForClientAndResource(Long resourceId, Long clientId);


}
