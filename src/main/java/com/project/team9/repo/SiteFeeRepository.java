package com.project.team9.repo;

import com.project.team9.model.buissness.SiteFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteFeeRepository extends JpaRepository<SiteFee, Long> {

}
