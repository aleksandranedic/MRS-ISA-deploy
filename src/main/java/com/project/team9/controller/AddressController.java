package com.project.team9.controller;

import com.project.team9.model.Address;
import com.project.team9.model.resource.Adventure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="address")
public class AddressController {

    @GetMapping
    public Address getAdventures() {
        return null;
    }
}
