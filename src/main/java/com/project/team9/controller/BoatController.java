package com.project.team9.controller;

import com.project.team9.dto.*;
import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.resource.Boat;
import com.project.team9.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path="boat")
public class BoatController {

    private final BoatService service;

    @Autowired
    public BoatController(BoatService service) {
        this.service = service;
    }

    @GetMapping("types")
    public ResponseEntity<List<String>> getBoatTypes(){return ResponseEntity.ok(service.getBoatTypes());}

    @GetMapping
    public List<Boat> getBoats() {
        return service.getBoats();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boat getBoat(@PathVariable String id) {
        Long houseId = Long.parseLong(id);
        return service.getBoat(houseId);
    }

    @GetMapping("/entity")
    public List<EntityDTO> getEntities() {
        return service.getEntities();
    }

    @PostMapping(value = "createBoat")
    public Long addBoatForOwner(BoatDTO boat, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        return service.createBoat(boat, multipartFiles);
    }

    @PostMapping(value = "updateBoat/{id}")
    public BoatDTO updateBoat(@PathVariable String id, BoatDTO boatDTO, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        return service.updateBoat(id, boatDTO, multipartFiles);
    }

    @PostMapping(value = "addQuickReservation/{id}")
    public Boolean addQuickReservation(@PathVariable String id, BoatQuickReservationDTO quickReservation) throws ReservationNotAvailableException {
        return service.addQuickReservation(Long.parseLong(id), quickReservation);
    }

    @PostMapping(value = "updateQuickReservation/{id}")
    public Boolean updateQuickReservation(@PathVariable String id, BoatQuickReservationDTO quickReservation) {
        return service.updateQuickReservation(Long.parseLong(id), quickReservation);
    }

    @PostMapping(value = "deleteQuickReservation/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Boolean deleteQuickReservation(@PathVariable String id, @RequestBody String reservationID) {
        return service.deleteQuickReservation(Long.parseLong(id), reservationID);
    }

    @GetMapping(value = "boatprof/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoatDTO getBoatDTO(@PathVariable String id) {
        Long boatId = Long.parseLong(id);
        return service.getBoatDTO(boatId);
    }

    @GetMapping("/subs/{id}")
    public List<EntityDTO> getSubs(@PathVariable Long id) {
        return service.findBoatsThatClientIsSubbedTo(id);
    }

    @GetMapping(value = "getOwner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceOwnerDTO getOwner(@PathVariable String id) {
        return service.getOwner(Long.parseLong(id));
    }

    @GetMapping(value = "getReservations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getReservations(@PathVariable String id) {
        return service.getReservations(Long.parseLong(id));
    }

    @GetMapping(value = "getownerboats/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BoatCardDTO> getOwnerHouses(@PathVariable String id) {
        Long owner_id = Long.parseLong(id);
        return service.getOwnerBoats(owner_id);
    }

    @GetMapping(value = "getownerboat/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoatCardDTO getOwnerHouse(@PathVariable String id) {
        return service.getBoatCard(Long.parseLong(id));
    }

    @GetMapping("delete/{id}")
    public ResponseEntity<String> deleteBoat(@PathVariable Long id) {
        boolean action = service.deleteById(id);
        if (action)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body("Brod je rezervisan, ne može se izbrisati.");
    }

    @GetMapping("/reservation/boatOwner/{id}")
    public List<ReservationDTO> getReservationsForOwner(@PathVariable Long id) {
        return  service.getReservationsForOwner(id);
    }

    @GetMapping(value = "getOwnerResources/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceReportDTO> getOwnerResources(@PathVariable String id) {
        Long owner_id = Long.parseLong(id);
        return service.getOwnerResources(owner_id);
    }

    @GetMapping("/clientCanReviewVendor/{vendorId}/{clientId}")
    public boolean clientCanReviewVendor(@PathVariable Long vendorId, @PathVariable Long clientId){
        return service.clientCanReviewVendor(vendorId, clientId);
    }

    @GetMapping("/reservation/boat/{id}")
    public List<ReservationDTO> getReservationsForBoat(@PathVariable Long id) {
        List<ReservationDTO> reservations = service.getReservationsForBoat(id);
        reservations.addAll(service.getBusyPeriodForBoat(id));
        return reservations;
    }

    @PostMapping("/quickReservations/reserve")
    public Long reserveQuickReservation(@RequestBody ReserveQuickReservationDTO dto) {
        return service.reserveQuickReservation(dto);
    }

    @GetMapping("/reservation/client/{id}")
    public List<ReservationDTO> getReservationsForClient(@PathVariable Long id) {
        return  service.getReservationsForClient(id);
    }

    @PostMapping("/reservation/add")
    public Long addReservation(@RequestBody NewReservationDTO dto) throws ReservationNotAvailableException {
        return service.createReservation(dto);
    }

    @PostMapping("/reservation/busyPeriod/add")
    public Long addBusyPeriod(@RequestBody NewBusyPeriodDTO dto) throws ReservationNotAvailableException {
        return service.createBusyPeriod(dto);
    }

    @GetMapping("/clientCanReview/{resourceId}/{clientId}")
    public Boolean clientCanReview(@PathVariable Long resourceId, @PathVariable Long clientId){
        return service.clientCanReview(resourceId, clientId);
    }

    @GetMapping("address")
    public ResponseEntity<List<String>> getBoatAddress(){
        return ResponseEntity.ok(service.getBoatAddress());
    }
    @PostMapping("/filterBoats")
    public ResponseEntity<List<EntityDTO>> getFilteredAdventures(@RequestBody BoatFilterDTO boatFilterDTO) {
        return ResponseEntity.ok(service.getFilteredBoats(boatFilterDTO));
    }


    @GetMapping("/reservation/forReview/{id}")
    public List<ReservationDTO> getReservationsForReview(@PathVariable Long id) {
        return  service.getReservationsForReview(id);
    }

    @PostMapping("subscribe")
    public ResponseEntity<String> subscribeUserOnBoat(@RequestBody SubscribeDTO subscribeDTO){
        return ResponseEntity.ok(service.subscribeBoatUserOnBoat(subscribeDTO));
    }
    @PostMapping("/isSubscribed")
    public ResponseEntity<Boolean> isUserSubscribedToBoat(@RequestBody SubscribeDTO subscribeDTO) {
        return ResponseEntity.ok(service.isUserSubscribedToBoat(subscribeDTO));
    }
    @PostMapping("unsubscribe")
    public ResponseEntity<String> unsubscribeUserOnBoat(@RequestBody SubscribeDTO subscribeDTO){
        return ResponseEntity.ok(service.unsubscribeBoatUserOnBoat(subscribeDTO));
    }

    @GetMapping("/clientSubbedBoats/{id}")
    public ResponseEntity<List<EntityDTO>> getClientsSubscribedBoats(@PathVariable Long id){
        return ResponseEntity.ok(service.getClientsSubscribedBoats());
    }

    @PostMapping("/cancelReservation/{id}")
    public ResponseEntity<String> cancelBoatReservation(@PathVariable Long id){
        return ResponseEntity.ok(service.cancelBoatReservation(id));
    }
}