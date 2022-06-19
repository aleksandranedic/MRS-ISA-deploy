package com.project.team9.service;

import com.project.team9.model.buissness.SiteFee;
import com.project.team9.repo.SiteFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SiteFeeService {
    private final SiteFeeRepository repository;

    @Autowired
    public SiteFeeService(SiteFeeRepository repository) {
        this.repository = repository;
    }

    public SiteFee getSiteFee() {
        List<SiteFee> fees = repository.findAll();
        return Collections.max(fees, Comparator.comparing(SiteFee::getStartTime));
    }

    public Long add(int percentage) {
        return repository.save(new SiteFee(percentage)).getId();
    }
}
