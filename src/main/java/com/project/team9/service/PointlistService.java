package com.project.team9.service;

import com.project.team9.model.buissness.Pointlist;
import com.project.team9.repo.PointlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointlistService {
    private final PointlistRepository repository;

    @Autowired
    public PointlistService(PointlistRepository repository) {
        this.repository = repository;
    }

    public Pointlist getClientPointlist() {
        return repository.getLastClientPointlist();
    }

    public Pointlist getVendorPointlist() {
        return repository.getLastVendorPointlist();
    }

    public Long add(int points, String type) {
        return repository.save(new Pointlist(points, type)).getId();
    }
}
