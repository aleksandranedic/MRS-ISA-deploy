package com.project.team9.service;

import com.project.team9.dto.ComplaintDTO;
import com.project.team9.dto.ComplaintResponseDTO;
import com.project.team9.model.request.Complaint;
import com.project.team9.model.user.Client;
import com.project.team9.repo.ComplaintsRepository;
import com.project.team9.security.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintsService {
    private final ComplaintsRepository repository;
    private final ClientService clientService;
    private final FishingInstructorService fishingInstructorService;
    private final BoatOwnerService boatOwnerService;
    private final VacationHouseOwnerService vacationHouseOwnerService;
    private final AdventureService adventureService;
    private final BoatService boatService;
    private final VacationHouseService vacationHouseService;
    private final EmailService emailService;
    @Autowired
    public ComplaintsService(ComplaintsRepository repository, ClientService clientService, FishingInstructorService fishingInstructorService, BoatOwnerService boatOwnerService, VacationHouseOwnerService vacationHouseOwnerService, AdventureService adventureService, BoatService boatService, VacationHouseService vacationHouseService, EmailService emailService) {
        this.repository = repository;
        this.clientService = clientService;
        this.fishingInstructorService = fishingInstructorService;
        this.boatOwnerService = boatOwnerService;
        this.vacationHouseOwnerService = vacationHouseOwnerService;
        this.adventureService = adventureService;
        this.boatService = boatService;
        this.vacationHouseService = vacationHouseService;
        this.emailService = emailService;
    }

    public void deleteComplaint(Long id){
        Complaint complaint=repository.getById(id);
        complaint.setDeleted(true);
        repository.save(complaint);
    }

    @Transactional(readOnly = false)
    public void saveComplaint(Complaint complaint) {
        repository.save(complaint);
    }
    public List<ComplaintDTO> getAllComplaints() {

        List<ComplaintDTO> complaintDTOS=new ArrayList<>();
        for (Complaint complaint :
                repository.findAll().stream().filter(complaint -> !complaint.getDeleted()).collect(Collectors.toCollection(ArrayList::new))) {

            complaintDTOS.add(new ComplaintDTO(
                    complaint.getUserId(),
                    complaint.getEntityType(),
                    complaint.getEntityId(),
                    getEntityName(complaint.getEntityType(), complaint.getEntityId()),
                    getUserFullName(complaint.getUserId()),
                    complaint.getComment(),
                    complaint.getId()
            ));
        }
        return complaintDTOS;
    }
    private String getUserFullName(Long id){
        return clientService.getById(String.valueOf(id)).getName();
    }

    private String getEntityName(String entityType,Long entityId){
        switch (entityType){
            case "VACATION_HOUSE_OWNER":
                return vacationHouseOwnerService.getOwner(entityId).getName();
            case "FISHING_INSTRUCTOR":
                return fishingInstructorService.getById(String.valueOf(entityId)).getName();
            case "BOAT_OWNER":
                return  boatOwnerService.getOwner(entityId).getName();
            case "VACATION_HOUSE":
                return vacationHouseService.getVacationHouse(entityId).getTitle();
            case "BOAT":
                return boatService.getBoat(entityId).getTitle();
            case "ADVENTURE":
                return adventureService.getById(String.valueOf(entityId)).getTitle();
        }
        return "";
    }

    public String answerComplaint(ComplaintResponseDTO responseDTO) {
        Complaint complaint=repository.getById(responseDTO.getComplaintId());
        if (complaint.getDeleted())
            return "Zahtev je već obrađen.";
        complaint.setDeleted(true);
        try{
            saveComplaint(complaint);
        }
        catch (ObjectOptimisticLockingFailureException e)   {
            return "Zahtev je već obrađen.";
        }
        String fullName="";
        String email="";
        switch (responseDTO.getEntityType()){
            case "VACATION_HOUSE_OWNER":
                fullName=vacationHouseOwnerService.getOwner(responseDTO.getEntityId()).getName();
                email=vacationHouseOwnerService.getOwner(responseDTO.getEntityId()).getEmail();
                break;
            case "FISHING_INSTRUCTOR":
                fullName=fishingInstructorService.getById(String.valueOf(responseDTO.getEntityId())).getName();
                email=fishingInstructorService.getById(String.valueOf(responseDTO.getEntityId())).getEmail();
                break;
            case "BOAT_OWNER":
                fullName=boatOwnerService.getOwner(responseDTO.getEntityId()).getName();
                email=boatOwnerService.getOwner(responseDTO.getEntityId()).getEmail();
                break;
            case "VACATION_HOUSE":
                fullName=vacationHouseService.getVacationHouse(responseDTO.getEntityId()).getOwner().getName();
                email=vacationHouseService.getVacationHouse(responseDTO.getEntityId()).getOwner().getEmail();
                break;
            case "BOAT":
                fullName=boatService.getBoat(responseDTO.getEntityId()).getOwner().getName();
                email=boatService.getBoat(responseDTO.getEntityId()).getOwner().getEmail();
                break;
            case "ADVENTURE":
                fullName=adventureService.getById(String.valueOf(responseDTO.getEntityId())).getOwner().getName();
                email=adventureService.getById(String.valueOf(responseDTO.getEntityId())).getOwner().getEmail();
                break;
        }
        Client client= clientService.getById(String.valueOf(responseDTO.getUserId()));
        String fullResponse="Administratorov odgovor na žalbu je: "+responseDTO.getResponse();
        String emailForVendorOfResourceOrVendor=emailService.buildHTMLEmail(fullName,fullResponse,"","Odgovor na žalbu klijenta");
       // emailService.send(email, emailForVendorOfResourceOrVendor, "Odgovor na žalbu klijenta");
        String emailForClient=emailService.buildHTMLEmail(client.getName(), fullResponse,"","Odgovor na žalbu klijenta");;
       // emailService.send(client.getEmail(),emailForClient, "Odgovor na žalbu klijenta");
        return "Uspešno ste odgovoroli na žalbu";
    }

    public Long addComplaint(Complaint complaint) {
        return repository.save(complaint).getId();
    }
}
