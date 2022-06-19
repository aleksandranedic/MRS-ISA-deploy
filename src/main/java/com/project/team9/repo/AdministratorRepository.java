package com.project.team9.repo;

import com.project.team9.model.user.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator,Long> {
    Administrator findByEmail(String email);

    @Query("FROM Administrator WHERE id = ?1")
    Administrator getAdminById(Long id);
}
