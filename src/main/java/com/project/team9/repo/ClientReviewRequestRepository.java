package com.project.team9.repo;

import com.project.team9.model.request.ClientReviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientReviewRequestRepository extends JpaRepository<ClientReviewRequest, Long> {
    @Query("FROM ClientReviewRequest WHERE resourceId = ?1 AND clientId = ?2")
    List<ClientReviewRequest> findReviewRequests(Long resourceId, Long clientId);
}
