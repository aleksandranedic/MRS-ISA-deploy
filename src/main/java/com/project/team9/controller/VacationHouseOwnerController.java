package com.project.team9.controller;

import com.project.team9.dto.*;
import com.project.team9.exceptions.CannotDeleteException;
import com.project.team9.model.resource.VacationHouse;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.service.VacationHouseOwnerService;
import com.project.team9.service.VacationHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;


@RestController
@RequestMapping(path = "houseowner")
public class VacationHouseOwnerController {

    private final VacationHouseOwnerService service;
    private final VacationHouseService vacationHouseService;

    @Autowired
    public VacationHouseOwnerController(VacationHouseOwnerService vacationHouseOwnerService, VacationHouseService vacationHouseService) {
        this.service = vacationHouseOwnerService;
        this.vacationHouseService = vacationHouseService;
    }

    @GetMapping
    public List<VacationHouseOwner> getAll() {
        return service.getAll();
    }

    @PostMapping(value = "getIncomeReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public IncomeReport getIncomeReport(@PathVariable String id, IncomeReportDateRange dateRange) {
        return vacationHouseService.getIncomeReport(Long.parseLong(id), dateRange);
    }

    @PostMapping(value = "getAttendanceReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public IncomeReport getAttendanceReport(@PathVariable String id, AttendanceReportParams attendanceReportParams) {
        return vacationHouseService.getAttendanceReport(Long.parseLong(id), attendanceReportParams);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VacationHouseOwner getOwner(@PathVariable String id) {
        Long ownerId = Long.parseLong(id);
        return service.getOwner(ownerId);
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> getVHONames() {
        return ResponseEntity.ok(service.getVHONames());
    }

    @PostMapping(value = "changeProfilePicture/{id}")
    public Boolean changeProfilePicture(@PathVariable String id, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        return service.changeProfilePicture(Long.parseLong(id), multipartFile);
    }

    @PostMapping("addHouse/{id}")
    public void addVacationHouseForOwner(@PathVariable String id, @RequestBody String houseId) {
        houseId = houseId.substring(0, houseId.length() - 1);
        VacationHouseOwner vho = service.getOwner(Long.parseLong(id));
        VacationHouse vh = vacationHouseService.getVacationHouse(Long.parseLong(houseId));
        vh.setOwner(vho);
        vacationHouseService.save(vh);
        vho.addVacationHouse(vh);
        service.addOwner(vho);
    }

    @PostMapping(value = "updateOwner/{id}")
    public void updateOwner(@PathVariable String id, UpdateOwnerDTO owner) {
        service.updateOwner(Long.parseLong(id), owner);
    }

    @PostMapping(value = "checkPassword/{id}", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkPassword(@PathVariable String id, @RequestBody String oldPassword) {
        return service.checkPassword(Long.parseLong(id), oldPassword);
    }

    @PostMapping(value = "updatePassword/{id}", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePassword(@PathVariable String id, @RequestBody String newPassword) {
        service.updatePassword(Long.parseLong(id), newPassword);
    }

    @PostMapping("/{id}/add")
    public void addOwner(@RequestBody VacationHouseOwner owner) {
        service.addOwner(owner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOwner(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getStat/{id}")
    public UserStatDTO getUserStat(@PathVariable Long id) {
        return service.getUserStat(id);
    }

    @PostMapping("/delete/{id}")
    Long delete(@PathVariable Long id) throws CannotDeleteException {
        return service.deleteById(id);
    }
}

