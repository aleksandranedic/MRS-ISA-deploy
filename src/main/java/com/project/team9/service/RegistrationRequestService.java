package com.project.team9.service;

import com.project.team9.model.request.RegistrationRequest;
import com.project.team9.model.request.Request;
import com.project.team9.repo.RegistrationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationRequestService {
    private final RegistrationRequestRepository repository;

    @Autowired
    public RegistrationRequestService(RegistrationRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = false)
    public RegistrationRequest addRegistrationRequest(RegistrationRequest registrationRequest) {
        return repository.save(registrationRequest);
    }

    public List<RegistrationRequest> getAllUndeletedRegistrationRequests() {
        return repository.findAll().stream().filter(registrationRequest -> !registrationRequest.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public String deleteRegistrationRequest(String id) {
        RegistrationRequest registrationRequest = repository.findById(Long.parseLong(id)).orElse(null);
        if (registrationRequest == null)
            return "Zahtev nije uspešno odbijen";
        else {
            registrationRequest.setDeleted(true);
            repository.save(registrationRequest);
            return "Uspešno ste odbili registraciju";
        }
    }

    public RegistrationRequest getRegistrationRequest(String id) {
        return repository.findById(Long.parseLong(id)).orElse(null);
    }
}
