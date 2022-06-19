package com.project.team9.repo;

import com.project.team9.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository  extends JpaRepository<Image, Long> {
    Optional<Image> findByPath(String path);
}
