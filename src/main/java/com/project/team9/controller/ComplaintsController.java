package com.project.team9.controller;

import com.project.team9.dto.ComplaintDTO;
import com.project.team9.dto.ComplaintResponseDTO;
import com.project.team9.model.request.Complaint;
import com.project.team9.service.ComplaintsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "complaints")
public class ComplaintsController {
    private final ComplaintsService complaintsService;

    public ComplaintsController(ComplaintsService complaintsService) {
        this.complaintsService = complaintsService;
    }

    @GetMapping
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
        return ResponseEntity.ok(complaintsService.getAllComplaints());
    }

    @PostMapping
    public ResponseEntity<String> answerComplaint(@RequestBody ComplaintResponseDTO responseDTO) {
        String response = complaintsService.answerComplaint(responseDTO);
        if (response.equalsIgnoreCase("Zahtev je već obrađen."))
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public Long addComplaint(@RequestBody Complaint complaint) {
        return complaintsService.addComplaint(complaint);
    }
}
