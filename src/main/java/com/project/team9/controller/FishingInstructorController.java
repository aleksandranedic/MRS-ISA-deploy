package com.project.team9.controller;

import com.project.team9.dto.*;
import com.project.team9.exceptions.CannotDeleteException;
import com.project.team9.exceptions.UserNotFoundException;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.service.FishingInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "fishinginstructor")
public class FishingInstructorController {

    private final FishingInstructorService service;

    @Autowired
    public FishingInstructorController(FishingInstructorService service) {
        this.service = service;
    }

    @GetMapping
    public List<FishingInstructor> getFishingInstructors() {
        return service.getFishingInstructors();
    }

    @PostMapping(value = "changeProfilePicture/{id}")
    public Boolean changeProfilePicture(@PathVariable String id, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        return service.changeProfilePicture(id, multipartFile);
    }

    @PostMapping(value = "getIncomeReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public IncomeReport getIncomeReport(@PathVariable String id, IncomeReportDateRange dateRange) {
        return service.getIncomeReport(Long.parseLong(id), dateRange);
    }

    @PostMapping(value = "getAttendanceReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public IncomeReport getAttendanceReport(@PathVariable String id, AttendanceReportParams attendanceReportParams) {
        return service.getAttendanceReport(Long.parseLong(id), attendanceReportParams);
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> getFishingInstructorNames(){return ResponseEntity.ok(service.getFINames());}

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FishingInstructor getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/edit")
    Long editFishingInstructor(@RequestBody FishingInstructorDTO dto) {
        return service.edit(dto);
    }

    @PostMapping("/delete/{id}")
    Long delete(@PathVariable Long id) throws CannotDeleteException {
        return service.deleteById(id);
    }

    @GetMapping("/getStat/{id}")
    public UserStatDTO getUserStat(@PathVariable Long id) {
        return service.getUserStat(id);
    }
}
