package com.project.team9.repo;

import com.project.team9.model.user.vendor.VacationHouseOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationHouseOwnerRepository extends JpaRepository<VacationHouseOwner, Long> {
    VacationHouseOwner findByEmail(String username);
}
