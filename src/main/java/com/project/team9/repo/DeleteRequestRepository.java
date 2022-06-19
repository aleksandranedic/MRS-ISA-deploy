package com.project.team9.repo;

import com.project.team9.model.request.DeleteRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteRequestRepository extends JpaRepository< DeleteRequest,Long> {

}
