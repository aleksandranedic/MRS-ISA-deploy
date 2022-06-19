package com.project.team9.service;

import com.project.team9.model.buissness.Pricelist;
import com.project.team9.repo.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricelistService {

    private final PricelistRepository pricelistRepository;

    @Autowired
    public PricelistService(PricelistRepository pricelistRepository) {
        this.pricelistRepository = pricelistRepository;
    }

    public void addPriceList(Pricelist pricelist) {
        pricelistRepository.save(pricelist);
    }

}
