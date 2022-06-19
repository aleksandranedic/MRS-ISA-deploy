package com.project.team9.controller;

import com.project.team9.dto.*;
import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.resource.VacationHouse;
import com.project.team9.service.VacationHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "house")
public class VacationHouseController {

    private final VacationHouseService service;


    @Autowired
    public VacationHouseController(VacationHouseService vacationHouseService) {
        this.service = vacationHouseService;
    }

    @GetMapping
    public List<VacationHouse> getHouses() {
        return service.getVacationHouses();
    }

    @GetMapping("/entity")
    public List<EntityDTO> getEntities() {
        return service.getEntities();
    }

    @GetMapping("/subs/{id}")
    public List<EntityDTO> getSubs(@PathVariable Long id) {
        return service.findVacationHousesThatClientIsSubbedTo(id);
    }


    @GetMapping("/reservation/vacationHouseOwner/{id}")
    public List<ReservationDTO> getReservationsForOwner(@PathVariable Long id) {
        return service.getReservationsForOwner(id);
    }

    @GetMapping("/clientCanReviewVendor/{vendorId}/{clientId}")
    public boolean clientCanReviewVendor(@PathVariable Long vendorId, @PathVariable Long clientId) {
        return service.clientCanReviewVendor(vendorId, clientId);
    }

    @PostMapping("/quickReservations/reserve")
    public Long reserveQuickReservation(@RequestBody ReserveQuickReservationDTO dto) {
        return service.reserveQuickReservation(dto);
    }

    @GetMapping("/reservation/vacationHouse/{id}")
    public List<ReservationDTO> getReservationsForVacationHouse(@PathVariable Long id) {
        List<ReservationDTO> reservations = service.getReservationsForVacationHouse(id);
        reservations.addAll(service.getBusyPeriodForVacationHouse(id));
        return reservations;
    }

    @GetMapping("/reservation/client/{id}")
    public List<ReservationDTO> getReservationsForClient(@PathVariable Long id) {
        return service.getReservationsForClient(id);
    }

    @PostMapping("/reservation/add")
    public Long addReservation(@RequestBody NewReservationDTO dto) throws ReservationNotAvailableException {
        return service.createReservation(dto);
    }

    @PostMapping("/reservation/busyPeriod/add")
    public Long addBusyPeriod(@RequestBody NewBusyPeriodDTO dto) throws ReservationNotAvailableException {
        return service.createBusyPeriod(dto);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VacationHouse getVacationHouse(@PathVariable String id) {
        Long houseId = Long.parseLong(id);
        return service.getVacationHouse(houseId);
    }


    @GetMapping(value = "getRating/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public double getRating(@PathVariable String id) {
        return service.getRatingForHouse(Long.parseLong(id));
    }


    @GetMapping(value = "getReservations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getReservations(@PathVariable String id) {
        return service.getReservations(Long.parseLong(id));
    }

    @PostMapping(value = "createHouse")
    public Long addVacationHouseForOwner(VacationHouseDTO house, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        return service.createHouse(house, multipartFiles);
    }

    @PostMapping(value = "updateHouse/{id}")
    public VacationHouseDTO updateVacationHouse(@PathVariable String id, VacationHouseDTO house, @RequestParam("fileImage") MultipartFile[] multipartFiles) throws IOException {
        return service.updateHouse(id, house, multipartFiles);
    }

    @PostMapping(value = "addQuickReservation/{id}")
    public Boolean addQuickReservation(@PathVariable String id, VacationHouseQuickReservationDTO quickReservation) throws ReservationNotAvailableException {
        return service.addQuickReservation(Long.parseLong(id), quickReservation);
    }

    @PostMapping(value = "updateQuickReservation/{id}")
    public Boolean updateQuickReservation(@PathVariable String id, VacationHouseQuickReservationDTO quickReservation) {
        return service.updateQuickReservation(Long.parseLong(id), quickReservation);
    }

    @PostMapping(value = "deleteQuickReservation/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Boolean deleteQuickReservation(@PathVariable String id, @RequestBody String reservationID) {
        return service.deleteQuickReservation(id, reservationID);
    }


    @GetMapping(value = "houseprof/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VacationHouseDTO getVacationHouseDTO(@PathVariable String id) {
        Long houseId = Long.parseLong(id);
        return service.getVacationHouseDTO(houseId);
    }

    @GetMapping(value = "getOwner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceOwnerDTO getOwner(@PathVariable String id) {
        return service.getOwner(Long.parseLong(id));
    }

    @GetMapping(value = "getownerhouses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HouseCardDTO> getOwnerHouses(@PathVariable String id) {
        Long owner_id = Long.parseLong(id);
        return service.getOwnerHouses(owner_id);
    }

    @GetMapping(value = "getOwnerResources/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceReportDTO> getOwnerResources(@PathVariable String id) {
        Long owner_id = Long.parseLong(id);
        return service.getOwnerResources(owner_id);
    }

    @GetMapping(value = "getownerhouse/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HouseCardDTO getOwnerHouse(@PathVariable String id) {
        return service.getVacationHouseCard(Long.parseLong(id));
    }

    @GetMapping("delete/{id}")
    public ResponseEntity<String> deleteVacationHouse(@PathVariable Long id) {
        boolean action = service.deleteById(id);
        if (action)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body("Vikendica je rezervisana, ne može se izbrisati.");
    }

    @GetMapping("/clientCanReview/{resourceId}/{clientId}")
    public Boolean clientCanReview(@PathVariable Long resourceId, @PathVariable Long clientId) {
        return service.clientCanReview(resourceId, clientId);
    }

    @GetMapping("/reservation/forReview/{id}")
    public List<ReservationDTO> getReservationsForReview(@PathVariable Long id) {
        return service.getReservationsForReview(id);
    }

    @GetMapping("address")
    public ResponseEntity<List<String>> getVacationHouseAddress() {
        return ResponseEntity.ok(service.getVacationHouseAddress());
    }

    @PostMapping("/filterHouse")
    public ResponseEntity<List<EntityDTO>> getFilteredVacationHouse(@RequestBody VacationHouseFilterDTO vacationHouseFilterDTO) {
        return ResponseEntity.ok(service.getFilteredVacationHouses(vacationHouseFilterDTO));
    }

    @PostMapping("subscribe")
    public ResponseEntity<String> subscribeUserOnVacationHouse(@RequestBody SubscribeDTO subscribeDTO){
        return ResponseEntity.ok(service.subscribeBoatUserOnVacationHouse(subscribeDTO));
    }
    @PostMapping("/isSubscribed")
    public ResponseEntity<Boolean> isUserSubscribedToVacationHouse(@RequestBody SubscribeDTO subscribeDTO) {
        return ResponseEntity.ok(service.isUserSubscribedToVacationHouse(subscribeDTO));
    }
    @PostMapping("unsubscribe")
    public ResponseEntity<String> unsubscribeUserOnVacationHouse(@RequestBody SubscribeDTO subscribeDTO){
        return ResponseEntity.ok(service.unsubscribeBoatUserOnVacationHouse(subscribeDTO));
    }

    @GetMapping("/clientSubbedVacationHouses/{id}")
    public ResponseEntity<List<EntityDTO>> getClientsSubscribedVacationHouses(@PathVariable Long id){
        return ResponseEntity.ok(service.getClientsSubscribedVacationHouses());
    }

    @PostMapping("/cancelReservation/{id}")
    public ResponseEntity<String> cancelVacationHouseReservation(@PathVariable Long id){
        return ResponseEntity.ok(service.cancelVacationHouseReservation(id));
    }
}