package com.project.team9.controller;

import com.project.team9.model.buissness.Pointlist;
import com.project.team9.service.PointlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "pointlist")
public class PointlistController {

    private final PointlistService pointlistService;

    @Autowired
    public PointlistController(PointlistService pointlistService) {
        this.pointlistService = pointlistService;
    }

    @GetMapping("/client")
    public Pointlist getClientPointlist() {
        return pointlistService.getClientPointlist();
    }

    @GetMapping("/vendor")
    public Pointlist getVendorPointlist() {
        return pointlistService.getVendorPointlist();
    }

    @PostMapping("/add/client/{points}")
    public Long addClientPointlist(@PathVariable int points){
        return pointlistService.add(points, "CLIENT");
    }

    @PostMapping("/add/vendor/{points}")
    public Long addVendorPointlist(@PathVariable int points){
        return pointlistService.add(points, "VENDOR");
    }
}
