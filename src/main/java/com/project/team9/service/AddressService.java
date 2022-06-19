package com.project.team9.service;

import com.project.team9.model.Address;
import com.project.team9.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAddresses() {
        return addressRepository.findAll();
    }

    public Address getById(String id) {
        return addressRepository.getById(Long.parseLong(id));
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address getByAttributes(Address address) {
        for (Address address1 :
                getAddresses()) {
            if (address1.getCountry().equals(address.getCountry()) &&
                    address1.getNumber().equals(address.getNumber()) &&
                    address1.getPlace().equals(address.getPlace()) &&
                    address1.getStreet().equals(address.getStreet()))
                return address1;
        }
        return null;
    }
}

