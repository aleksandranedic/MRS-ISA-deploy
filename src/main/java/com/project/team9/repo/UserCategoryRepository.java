package com.project.team9.repo;

import com.project.team9.model.buissness.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    @Query("FROM UserCategory WHERE isClientCategory = true AND deleted = false")
    List<UserCategory> getAllClientCategories();

    @Query("FROM UserCategory WHERE isVendorCategory = true AND deleted = false")
    List<UserCategory> getAllVendorCategories();
}
