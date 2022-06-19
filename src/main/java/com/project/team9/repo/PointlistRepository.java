package com.project.team9.repo;

import com.project.team9.model.buissness.Pointlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointlistRepository extends JpaRepository<Pointlist, Long> {
    @Query(value = "SELECT * FROM Pointlist WHERE type='CLIENT' ORDER BY start_time DESC LIMIT 1", nativeQuery = true)
    Pointlist getLastClientPointlist();

    @Query(value = "SELECT * FROM Pointlist WHERE type='VENDOR' ORDER BY start_time DESC LIMIT 1", nativeQuery = true)
    Pointlist getLastVendorPointlist();
}
