package com.project.team9.repo;

import com.project.team9.model.request.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintsRepository extends JpaRepository<Complaint,Long> {
}
