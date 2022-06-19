package com.project.team9.controller;

import com.project.team9.dto.DeleteReplayDTO;
import com.project.team9.model.request.DeleteRequest;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="deletionRequests")
public class DeletionRequestController {
    private final DeleteRequestService service;

    @Autowired
    public DeletionRequestController(DeleteRequestService service) {
        this.service = service;
    }

    @GetMapping
    private ResponseEntity<List<DeleteRequest>> getAllDeletionRequests() {
        return ResponseEntity.ok(service.getAllDeletionRequests());
    }

    @PostMapping(path="/validateDeletion",produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> deleteUser(@RequestBody DeleteReplayDTO deleteReplayDTO) {
        String response = service.processDeletionRequest(deleteReplayDTO);
        if (response.equalsIgnoreCase("Zahtev za brisanje je već obrađen."))
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id, @RequestParam String deletingReason) {
        return ResponseEntity.ok(service.deleteUser(id,deletingReason, "CLIENT"));
    }

    @DeleteMapping(value = "/houseowner/{id}", consumes=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteHouseOwner(@PathVariable Long id, @RequestBody String deletingReason) {
        return ResponseEntity.ok(service.deleteUser(id,deletingReason, "VACATION_HOUSE_OWNER"));
    }
    @DeleteMapping(value="/boatowner/{id}", consumes=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteBoatOwner(@PathVariable Long id, @RequestBody String deletingReason) {
        return ResponseEntity.ok(service.deleteUser(id,deletingReason, "BOAT_OWNER"));
    }
    @PostMapping("/fishingInstructor/{id}")
    public ResponseEntity<String> createFishingInstructorRequest(@PathVariable Long id, @RequestBody String deletingReason) {
        return ResponseEntity.ok(service.deleteUser(id,deletingReason, "FISHING_INSTRUCTOR"));
    }



}
