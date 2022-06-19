package com.project.team9.service;

import com.project.team9.dto.VendorRegistrationRequestReplay;
import com.project.team9.model.Address;
import com.project.team9.model.request.RegistrationRequest;
import com.project.team9.model.resource.Boat;
import com.project.team9.model.user.Role;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VendorRegistrationService {
    private final RegistrationRequestService service;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final FishingInstructorService fishingInstructorService;
    private final VacationHouseOwnerService vacationHouseOwnerService;
    private final BoatOwnerService boatOwnerService;
    private final AddressService addressService;
    private final EmailService emailService;

    @Value("${frontendlink}")
    private String frontLink;

    @Autowired
    public VendorRegistrationService(RegistrationRequestService service, RoleService roleService, PasswordEncoder passwordEncoder, FishingInstructorService fishingInstructorService, VacationHouseOwnerService vacationHouseOwnerService, BoatOwnerService boatOwnerService, AddressService addressService, EmailService emailService) {
        this.service = service;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.fishingInstructorService = fishingInstructorService;
        this.vacationHouseOwnerService = vacationHouseOwnerService;
        this.boatOwnerService = boatOwnerService;
        this.addressService = addressService;
        this.emailService = emailService;
    }

    public List<RegistrationRequest> getAllVendorRegistrations() {
        return service.getAllUndeletedRegistrationRequests();
    }

    public String validateVendor(VendorRegistrationRequestReplay replay, RegistrationRequest registrationRequest) {
        String name="";
        if (registrationRequest == null) {
            return "Odobravanje registracija nije uspešno";
        } else if (fishingInstructorService.getFishingInstructorByEmail(replay.getUsername()) != null
                || vacationHouseOwnerService.getVacationHouseOwnerByEmail(replay.getUsername()) != null
                || boatOwnerService.getBoatOwnerByEmail(replay.getUsername()) != null) {
            return "Коrisnik sa unetim emailom već postoji";
        } else {
            Address requestAddress = new Address(registrationRequest.getPlace(),
                    registrationRequest.getNumber(),
                    registrationRequest.getStreet(),
                    registrationRequest.getCountry());
            Role role = roleService.findRoleByName(registrationRequest.getUserRole());
            if (role == null) {
                role = new Role(registrationRequest.getUserRole());
                roleService.save(role);
            }
            Address address = addressService.getByAttributes(requestAddress);
            if (address == null) {
                addressService.addAddress(requestAddress);
            } else {
                requestAddress = address;
            }
            switch (registrationRequest.getUserRole()) {
                case "FISHING_INSTRUCTOR":
                    FishingInstructor fishingInstructor = new FishingInstructor(
                            null,
                            passwordEncoder.bCryptPasswordEncoder().encode(registrationRequest.getPassword()),
                            registrationRequest.getFirstName(),
                            registrationRequest.getLastName(),
                            registrationRequest.getEmail(),
                            registrationRequest.getPhoneNumber(),
                            requestAddress,
                            false,
                            registrationRequest.getRegistrationRationale(),
                            registrationRequest.getBiography(),
                            role,
                            new ArrayList<>()
                    );
                    name=fishingInstructor.getName();
                    fishingInstructor.setEnabled(true);
                    fishingInstructor.setLastPasswordResetDate(new Timestamp(new Date().getTime()));
                    fishingInstructorService.addFishingInstructor(fishingInstructor);
                    break;
                case "VACATION_HOUSE_OWNER":
                    VacationHouseOwner vacationHouseOwner = new VacationHouseOwner(
                            null,
                            passwordEncoder.bCryptPasswordEncoder().encode(registrationRequest.getPassword()),
                            registrationRequest.getFirstName(),
                            registrationRequest.getLastName(),
                            registrationRequest.getEmail(),
                            registrationRequest.getPhoneNumber(),
                            requestAddress,
                            false,
                            registrationRequest.getRegistrationRationale(),
                            role
                    );
                    name= vacationHouseOwner.getName();
                    vacationHouseOwner.setEnabled(true);
                    vacationHouseOwner.setLastPasswordResetDate(new Timestamp(new Date().getTime()));
                    vacationHouseOwnerService.addOwner(vacationHouseOwner);
                    break;
                case "BOAT_OWNER":
                    BoatOwner boatOwner = new BoatOwner(
                            null,
                            passwordEncoder.bCryptPasswordEncoder().encode(registrationRequest.getPassword()),
                            registrationRequest.getFirstName(),
                            registrationRequest.getLastName(),
                            registrationRequest.getEmail(),
                            registrationRequest.getPhoneNumber(),
                            requestAddress,
                            false,
                            registrationRequest.getRegistrationRationale(),
                            new ArrayList<Boat>(),
                            role
                    );
                    name = boatOwner.getName();
                    boatOwner.setLastPasswordResetDate(new Timestamp(new Date().getTime()));
                    boatOwner.setEnabled(true);
                    boatOwnerService.save(boatOwner);
                    break;
            }
            String fullResponse = "Administratorov odgovor je: " + replay.getResponse() + "\n Uspešno ste registrovani. ";
            String additionalText = "<a href=\"" + frontLink+"login" + "\">";
            String email = emailService.buildHTMLEmail(name, fullResponse, additionalText, "Registracija pružaoca usluga");
            //emailService.send(registrationRequest.getEmail(), email, "Registracija pružaoca usluga");
            return "Odobravanje registracije je uspešno";
        }
    }

    public String deleteRegistrationRequest(VendorRegistrationRequestReplay replay,  RegistrationRequest registrationRequest) {
        String fullResponse = "Administratorov odgovor je: " + replay.getResponse();
        String additionalText="Niste registrovani jer Vam je administrator odbio registraciju";
        String name=replay.getFullName();
        String email = emailService.buildHTMLEmail(name, fullResponse, additionalText, "Registracija pružaoca usluga");
        //emailService.send(registrationRequest.getEmail(), email, "Registracija pružaoca usluga");
        return service.deleteRegistrationRequest(replay.getRequestId());
    }

    public String processRegistrationRequest(VendorRegistrationRequestReplay replay) {
        RegistrationRequest registrationRequest = service.getRegistrationRequest(replay.getRequestId());
        if (registrationRequest.getDeleted())
            return "Zahtev je već obrađen.";
        registrationRequest.setDeleted(true);
        registrationRequest.setResponse(replay.getResponse());
        try{
            service.addRegistrationRequest(registrationRequest);
        }
        catch (ObjectOptimisticLockingFailureException e)   {
            return "Zahtev je već obrađen.";
        }
        if (replay.getType().equals("approve")) {
            return validateVendor(replay, registrationRequest);
        } else {
            return deleteRegistrationRequest(replay, registrationRequest);
        }
    }
}
