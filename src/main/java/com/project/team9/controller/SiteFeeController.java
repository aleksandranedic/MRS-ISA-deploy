package com.project.team9.controller;

import com.project.team9.model.buissness.SiteFee;
import com.project.team9.service.SiteFeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "siteFee")
public class SiteFeeController {

    private final SiteFeeService service;

    public SiteFeeController(SiteFeeService service) {
        this.service = service;
    }

    @GetMapping
    public SiteFee getSiteFee() {
        return service.getSiteFee();
    }

    @PostMapping("/add/{percentage}")
    public Long addSiteFee(@PathVariable int percentage) {
        return service.add(percentage);
    }
}
