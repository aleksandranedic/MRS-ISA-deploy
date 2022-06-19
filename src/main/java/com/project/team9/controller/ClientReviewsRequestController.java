package com.project.team9.controller;

import com.project.team9.dto.ClientReviewRequestDTO;
import com.project.team9.dto.ClientReviewResponseDTO;
import com.project.team9.service.ClientReviewRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientReviews")
public class ClientReviewsRequestController {

    private final ClientReviewRequestService service;

    @Autowired
    public ClientReviewsRequestController(ClientReviewRequestService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClientReviewRequestDTO>> getAllClientReviewRequests(){
        return ResponseEntity.ok(service.getAllClientReviewRequests());
    }
    @PostMapping(path = "approve")
    public ResponseEntity<String>  approveClientReview(@RequestBody ClientReviewResponseDTO clientReviewResponseDTO ){
        return ResponseEntity.ok(service.approveClientReview(clientReviewResponseDTO));
    }

    @PostMapping(path = "deny")
    public ResponseEntity<String>  denyClientReview(@RequestBody ClientReviewResponseDTO clientReviewResponseDTO ){
        return ResponseEntity.ok(service.denyClientReview(clientReviewResponseDTO));
    }
}
