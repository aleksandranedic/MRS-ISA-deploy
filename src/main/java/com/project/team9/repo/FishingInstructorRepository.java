package com.project.team9.repo;

import com.project.team9.model.user.vendor.FishingInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishingInstructorRepository extends JpaRepository<FishingInstructor, Long> {
    FishingInstructor findByEmail(String email);

    @Query("FROM FishingInstructor WHERE id = ?1")
    FishingInstructor get(Long id);
}
