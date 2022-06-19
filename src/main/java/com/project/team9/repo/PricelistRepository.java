package com.project.team9.repo;

import com.project.team9.model.buissness.Pricelist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricelistRepository extends JpaRepository<Pricelist, Long> {
}
