package com.project.team9.service;

import com.project.team9.model.request.VendorReviewRequest;
import com.project.team9.repo.VendorReviewRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorReviewRequestService {

    private final VendorReviewRequestRepository repository;

    @Autowired
    public VendorReviewRequestService(VendorReviewRequestRepository vendorReviewRequestRepository) {
        this.repository = vendorReviewRequestRepository;
    }

    public Long addVendorReviewRequest(VendorReviewRequest vendorReviewRequest) {
        return this.repository.save(vendorReviewRequest).getId();
    }

    public boolean reservationHasReviewRequest(Long id) {

        return this.repository.findReviewForReservation(id).size() > 0;
    }

    public List<VendorReviewRequest> getAllVendorReviews() {
        List<VendorReviewRequest> all = repository.findAll();
        return all.stream().filter(vendorReviewRequest -> !vendorReviewRequest.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public VendorReviewRequest getById(Long id){return repository.getById(id);}

    public VendorReviewRequest delete(Long vendorReviewRequestId) {
        VendorReviewRequest vendorReviewRequest=getById(vendorReviewRequestId);
        vendorReviewRequest.setDeleted(true);
        repository.save(vendorReviewRequest);
        return vendorReviewRequest;
    }
}
