package com.project.team9.controller;

import com.project.team9.dto.*;
import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.resource.Adventure;
import com.project.team9.service.AdventureService;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "adventure")
public class AdventureController {

    private final AdventureService service;

    @Autowired
    public AdventureController(AdventureService adventureService) {
        this.service = adventureService;
    }

    @GetMapping("/subs/{id}")
    public List<EntityDTO> getSubs(@PathVariable Long id) {
        return service.findAdventuresThatClientIsSubbedTo(id);
    }

    @PostMapping("/reservation/add")
    public Long addReservation(@RequestBody NewReservationDTO dto) throws ReservationNotAvailableException {
        return service.createReservation(dto);
    }

    @PostMapping("/filterAdventures")
    public ResponseEntity<List<EntityDTO>> getFilteredAdventures(@RequestBody AdventureFilterDTO filterDTO) {
        return ResponseEntity.ok(service.getFilteredAdventures(filterDTO));
    }

    @PostMapping("/reservation/busyPeriod/add")
    public Long addBusyPeriod(@RequestBody NewBusyPeriodDTO dto) throws ReservationNotAvailableException {
        return service.createBusyPeriod(dto);
    }

    @GetMapping("/reservation/fishingInstructor/{id}")
    public List<ReservationDTO> getReservationsForInstructor(@PathVariable Long id) {
        List<ReservationDTO> reservationsForFishingInstructor = service.getReservationsForFishingInstructor(id);
        reservationsForFishingInstructor.addAll(service.getBusyPeriodsForFishingInstructor(id));
        return reservationsForFishingInstructor;
    }

    @GetMapping("/reservation/forReview/{id}")
    public List<ReservationDTO> getReservationsForReview(@PathVariable Long id) {
        return service.getReservationsForReview(id);
    }

    @GetMapping("/reservation/adventure/{id}")
    public List<ReservationDTO> getReservationsForAdventure(@PathVariable Long id) {
        List<ReservationDTO> reservationsForAdventure = service.getReservationsForAdventure(id);
        reservationsForAdventure.addAll(service.getBusyPeriodsForAdventure(id));
        return reservationsForAdventure;
    }

    @GetMapping(value = "getOwnerResources/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceReportDTO> getOwnerResources(@PathVariable String id) {
        Long owner_id = Long.parseLong(id);
        return service.getOwnerResources(owner_id);
    }

    @GetMapping("/reservation/client/{id}")
    public List<ReservationDTO> getReservationsForClient(@PathVariable Long id) {
        return service.getReservationsForClient(id);
    }

    @GetMapping
    public List<Adventure> getAdventures() {
        return service.getAdventures();
    }

    @GetMapping("/entity")
    public List<EntityDTO> getEntities() {
        return service.getEntities();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Adventure getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/quickReservations/reserve")
    public Long reserveQuickReservation(@RequestBody ReserveQuickReservationDTO dto) {
        return service.reserveQuickReservation(dto);
    }

    @GetMapping(value = "quickReservations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdventureQuickReservationDTO> getQuickReservations(@PathVariable String id) {
        return service.getQuickReservations(id);
    }

    @PostMapping(value = "addQuickReservation/{id}")
    public Boolean addQuickReservation(@PathVariable String id, AdventureQuickReservationDTO quickReservation) throws ReservationNotAvailableException {
        return service.addQuickReservation(id, quickReservation);
    }

    @PostMapping(value = "updateQuickReservation/{id}")
    public Boolean updateQuickReservation(@PathVariable String id, AdventureQuickReservationDTO quickReservation) {
        return service.updateQuickReservation(id, quickReservation);
    }
    @PostMapping(value = "deleteQuickReservation/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Boolean deleteQuickReservation(@PathVariable String id, @RequestBody String reservationID) {
        return service.deleteQuickReservation(id, reservationID);
    }
    @PostMapping(value = "updateAdventure/{id}")
    public AdventureDTO updateAdventure(@PathVariable String id, AdventureDTO adventureDTO, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        System.out.println(adventureDTO);
        return service.updateAdventure(id, adventureDTO, multipartFiles);
    }
    @GetMapping(value = "/dto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AdventureDTO getDTOById(@PathVariable String id) {
        return service.getDTOById(id);
    }

    @PostMapping("/{id}/edit")
    public Adventure edit(@RequestBody AdventureDTO adventure, @PathVariable String id, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        System.out.println(id);
        return service.editAdventure(id, adventure, multipartFiles);

    }

    @GetMapping("/clientCanReview/{resourceId}/{clientId}")
    public boolean clientCanReview(@PathVariable Long resourceId, @PathVariable Long clientId) {
        return service.clientCanReview(resourceId, clientId);
    }

    @GetMapping("/clientCanReviewVendor/{vendorId}/{clientId}")
    public boolean clientCanReviewVendor(@PathVariable Long vendorId, @PathVariable Long clientId) {
        return service.clientCanReviewVendor(vendorId, clientId);
    }

    @PostMapping("/add")
    public Long addAdventure(AdventureDTO adventure, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        return service.createAdventure(adventure, multipartFiles);
    }

    @GetMapping("/owner/{ownerId}")
    List<Adventure> findAdventuresWithOwner(@PathVariable String ownerId) {
        return service.findAdventuresWithOwner(ownerId);
    }

    @PostMapping("/delete/{id}")
    Long deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/address")
    public ResponseEntity<List<String>> getAdventureAddress() {
        return ResponseEntity.ok(service.getAdventureAddress());
    }

    @PostMapping("subscribe")
    public ResponseEntity<String> subscribeUserOnAdventure(@RequestBody SubscribeDTO subscribeDTO){
        return ResponseEntity.ok(service.subscribeUserOnAdventure(subscribeDTO));
    }
    @PostMapping("/isSubscribed")
    public ResponseEntity<Boolean> isUserSubscribedToAdventure(@RequestBody SubscribeDTO subscribeDTO) {
        return ResponseEntity.ok(service.isUserSubscribedToAdventure(subscribeDTO));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeUserOnAdventure(@RequestBody SubscribeDTO subscribeDTO){
        return ResponseEntity.ok(service.unsubscribeUserOnAdventure(subscribeDTO));
    }
    @GetMapping("/clientSubbedAdventures/{id}")
    public ResponseEntity<List<EntityDTO>> getClientsSubscribedAdventures(@PathVariable Long id){
        return ResponseEntity.ok(service.getClientsSubscribedAdventures());
    }
    @PostMapping("/cancelReservation/{id}")
    public ResponseEntity<String> cancelAdventureReservation(@PathVariable Long id){
        return ResponseEntity.ok(service.cancelAdventureReservation(id));
    }
}
