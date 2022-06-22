package com.project.team9.service;

import com.project.team9.dto.DeleteReplayDTO;
import com.project.team9.model.request.DeleteRequest;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.User;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.repo.DeleteRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeleteRequestService {
    private final DeleteRequestRepository deleteRequestRepository;
    private final FishingInstructorService fishingInstructorService;
    private final ClientService clientService;
    private final BoatOwnerService boatOwnerService;
    private final EmailService emailService;
    private final VacationHouseOwnerService vacationHouseOwnerService;
    private final AdventureReservationService adventureReservationService;
    private final BoatReservationService boatReservationService;
    private final VacationHouseReservationService vacationHouseReservationService;

    @Autowired
    public DeleteRequestService(DeleteRequestRepository deleteRequestRepository, FishingInstructorService fishingInstructorService, ClientService clientService, BoatOwnerService boatOwnerService, EmailService emailService, VacationHouseOwnerService vacationHouseOwnerService
            , AdventureReservationService adventureReservationService, BoatReservationService boatReservationService, VacationHouseReservationService vacationHouseReservationService
    ) {
        this.deleteRequestRepository = deleteRequestRepository;
        this.fishingInstructorService = fishingInstructorService;
        this.clientService = clientService;
        this.boatOwnerService = boatOwnerService;
        this.emailService = emailService;
        this.vacationHouseOwnerService = vacationHouseOwnerService;
        this.adventureReservationService = adventureReservationService;
        this.boatReservationService = boatReservationService;
        this.vacationHouseReservationService = vacationHouseReservationService;
    }

    @Transactional(readOnly = false)
    public void addDeleteRequest(DeleteRequest deleteRequest) {
        deleteRequestRepository.save(deleteRequest);
    }

    public List<DeleteRequest> getAllDeletionRequests() {
        return deleteRequestRepository.findAll().stream().filter(deleteRequest -> !deleteRequest.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public DeleteRequest getById(String requestId) {
        return deleteRequestRepository.getById(Long.parseLong(requestId));
    }

    public User getUserFromDeletionRequest(DeleteReplayDTO deleteReplayDTO){
        if (fishingInstructorService.getFishingInstructorByEmail(deleteReplayDTO.getUsername()) != null) {
            return fishingInstructorService.getFishingInstructorByEmail(deleteReplayDTO.getUsername());}
        else if (boatOwnerService.getBoatOwnerByEmail(deleteReplayDTO.getUsername()) != null) {
            return boatOwnerService.getBoatOwnerByEmail(deleteReplayDTO.getUsername());}
        else if (vacationHouseOwnerService.getVacationHouseOwnerByEmail(deleteReplayDTO.getUsername()) != null) {
            return vacationHouseOwnerService.getVacationHouseOwnerByEmail(deleteReplayDTO.getUsername());
        }else if (clientService.getClientByEmail(deleteReplayDTO.getUsername()) != null) {
            return clientService.getClientByEmail(deleteReplayDTO.getUsername());
        }
        return null;
    }
    public String processDeletionRequest(DeleteReplayDTO deleteReplayDTO) {
        String response = "";
        String email = "";
        String fullName = "";
        DeleteRequest deleteRequest = getById(deleteReplayDTO.getRequestId());
        if (deleteRequest.getDeleted())
            return "Zahtev za brisanje je već obrađen.";
        deleteReplayDTO.setComment(deleteReplayDTO.getComment());
        deleteRequest.setDeleted(true);
        try{
            addDeleteRequest(deleteRequest);
        }
        catch (ObjectOptimisticLockingFailureException e)   {
            return "Zahtev za brisanje je već obrađen.";
        }
        if (deleteReplayDTO.getType().equals("approve")) {
            if (fishingInstructorService.getFishingInstructorByEmail(deleteReplayDTO.getUsername()) != null) {
                FishingInstructor fishingInstructor = fishingInstructorService.getFishingInstructorByEmail(deleteReplayDTO.getUsername());
                email = fishingInstructor.getEmail();
                fullName = fishingInstructor.getName();
                int numberOfReservation=adventureReservationService.getAdventureReservationsForVendorId(fishingInstructor.getId()).size();
                if(numberOfReservation>0){
                    return "Nalog ne može da se obriše pošto instruktor pecanja ima zakazane rezervacije";
                }else{
                    fishingInstructor.setDeleted(true);
                    fishingInstructorService.addFishingInstructor(fishingInstructor);
                }
            } else if (boatOwnerService.getBoatOwnerByEmail(deleteReplayDTO.getUsername()) != null) {
                BoatOwner boatOwner = boatOwnerService.getBoatOwnerByEmail(deleteReplayDTO.getUsername());
                email = boatOwner.getEmail();
                fullName = boatOwner.getName();
                int numberOfReservation=boatReservationService.getBoatReservationsForVendorId(boatOwner.getId()).size();
                if(numberOfReservation>0){
                    return "Nalog ne može da se obriše pošto vlasnik broda ima zakazane rezervacije";
                }
                else{
                    boatOwner.setDeleted(true);
                    boatOwnerService.save(boatOwner);
                }
            } else if (vacationHouseOwnerService.getVacationHouseOwnerByEmail(deleteReplayDTO.getUsername()) != null) {
                VacationHouseOwner vacationHouseOwner = vacationHouseOwnerService.getVacationHouseOwnerByEmail(deleteReplayDTO.getUsername());
                email = vacationHouseOwner.getEmail();
                fullName = vacationHouseOwner.getName();
                int numberOfReservation=vacationHouseReservationService.getVacationHouseReservationsForVendorId(vacationHouseOwner.getId()).size();
                if(numberOfReservation>0){
                    return "Nalog ne može da se obriše pošto vlasnik vikendice ima zakazane rezervacije";
                }else {
                    vacationHouseOwner.setDeleted(true);
                    vacationHouseOwnerService.save(vacationHouseOwner);
                }

            } else if (clientService.getClientByEmail(deleteReplayDTO.getUsername()) != null) {
                Client client = clientService.getClientByEmail(deleteReplayDTO.getUsername());
                email = client.getEmail();
                fullName = client.getFirstName() + " " + client.getLastName();
                int numberOfReservation=vacationHouseReservationService.getVacationHouseReservationsForClienId(client.getId()).size()+
                                            adventureReservationService.getAdventureReservationsForClientId(client.getId()).size()+
                                            boatReservationService.getBoatReservationsForClientId(client.getId()).size();
                if(numberOfReservation>0){
                    return "Nalog ne može da se obriše pošto klijent ima zakazane rezervacije";
                }else{
                    client.setDeleted(true);
                    clientService.addClient(client);
                }
            }
            response = "Uspešno ste obrisali korisnika";
            String fullResponse="Administratoreva poruka je: "+deleteReplayDTO.getComment()+"\nVaš nalog će biti obrisan";
            String emailForUser=emailService.buildHTMLEmail(fullName,fullResponse,"Hvala Vam što ste bili deo našeg tima","Potvrda o brisanje naloga");
            emailService.send(email, emailForUser,"Potvrda o brisanje naloga");
        } else {
            String fullResponse="Administratoreva poruka je: "+deleteReplayDTO.getComment()+"\nVaš nalog neće biti obrisan";
            String emailForUser=emailService.buildHTMLEmail(fullName,fullResponse,"","Potvrda o brisanje naloga");
            emailService.send(email, emailForUser,"Potvrda o brisanje naloga");
            response = "Uspešno ste odbili brisanje korisnika";
        }
        return response;
    }

    public String deleteUser(Long id,String deletingReason, String UserType) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setUserId(id);
        deleteRequest.setUserType(UserType);
        deleteRequest.setComment(deletingReason);
        deleteRequest.setResponse("");
        deleteRequestRepository.save(deleteRequest);
        return "Uspešno ste poslali zahtev za brisanje vašeg naloga";
    }
}
