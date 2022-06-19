package com.project.team9.service;

import com.project.team9.controller.VendorReviewResponseDTO;
import com.project.team9.dto.VendorRequestReviewDenialDTO;
import com.project.team9.dto.VendorReviewDTO;
import com.project.team9.model.request.VendorReviewRequest;
import com.project.team9.model.review.VendorReview;
import com.project.team9.model.user.Client;
import com.project.team9.repo.VendorReviewRepository;
import com.project.team9.security.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PenaltyReviewService {

    private final VendorReviewRequestService vendorReviewRequestService;
    private final ClientService clientService;
    private final AdventureService adventureService;
    private final BoatService boatService;
    private final VacationHouseService vacationHouseService;
    private final VendorReviewRepository vendorReviewRepository;
    private final EmailService emailService;

    @Autowired
    public PenaltyReviewService(VendorReviewRequestService vendorReviewRequestService, ClientService clientService, AdventureService adventureService, BoatService boatService, VacationHouseService vacationHouseService, VendorReviewRepository vendorReviewRepository, EmailService emailService) {
        this.vendorReviewRequestService = vendorReviewRequestService;
        this.clientService = clientService;
        this.adventureService = adventureService;
        this.boatService = boatService;
        this.vacationHouseService = vacationHouseService;
        this.vendorReviewRepository = vendorReviewRepository;
        this.emailService = emailService;
    }

    public List<VendorReviewDTO> getPenaltyReviews() {
        List<VendorReviewDTO> penalties = new ArrayList<>();
        for (VendorReviewRequest vendorReviewRequest : vendorReviewRequestService.getAllVendorReviews()) {
            String[] list = getResourceTitleVendorNameVendorId(vendorReviewRequest.getResourceId());
            penalties.add(new VendorReviewDTO(
                    vendorReviewRequest.getClientId(),
                    vendorReviewRequest.getVendorId(),
                    vendorReviewRequest.getReservationId(),
                    vendorReviewRequest.getResourceId(), list[0],
                    list[1],
                    vendorReviewRequest.getRating(),
                    clientService.getById(vendorReviewRequest.getClientId().toString()).getName(),
                    vendorReviewRequest.isPenalty(),
                    vendorReviewRequest.isNoShow(),
                    vendorReviewRequest.getComment(),
                    vendorReviewRequest.getId()));
        };
        return penalties;
    }

    private String[] getResourceTitleVendorNameVendorId(Long resourceId) {
        String[] list = new String[4];
        try {
            list[0] = adventureService.getById(resourceId.toString()).getTitle();
            list[1] = adventureService.getById(resourceId.toString()).getOwner().getName();
            list[2] = adventureService.getById(resourceId.toString()).getOwner().getId().toString();
            list[3] = adventureService.getById(resourceId.toString()).getOwner().getEmail();
        } catch (Exception e) {
            list[0] = "";
            list[1] = "";
            list[2] = "";
            list[3] = "";
        }
        if (list[0].equals("")) {
            try {
                list[0] = boatService.getBoat(resourceId).getTitle();
                list[1] = boatService.getBoat(resourceId).getOwner().getName();
                list[2] = boatService.getBoat(resourceId).getOwner().getId().toString();
                list[3] = boatService.getBoat(resourceId).getOwner().getEmail();
            } catch (Exception e) {
                list[0] = "";
                list[1] = "";
                list[2] = "";
                list[3] = "";
            }
        }
        if (list[0].equals("")) {
            try {
                list[0] = vacationHouseService.getVacationHouse(resourceId).getTitle();
                list[1] = vacationHouseService.getVacationHouse(resourceId).getOwner().getName();
                list[2] = vacationHouseService.getVacationHouse(resourceId).getOwner().getId().toString();
                list[3] = vacationHouseService.getVacationHouse(resourceId).getOwner().getEmail();
            } catch (Exception e) {
                list[0] = "";
                list[1] = "";
                list[2] = "";
                list[3] = "";
            }
        }
        return list;
    }

    public String approveVendorReview(VendorReviewResponseDTO reviewResponseDTO) {
        String[] list = getResourceTitleVendorNameVendorId(reviewResponseDTO.getResourceId());
        VendorReview vendorReview = new VendorReview(
                reviewResponseDTO.getResourceId(),
                Long.parseLong(list[2]),
                reviewResponseDTO.getRating(),
                reviewResponseDTO.getText(),
                reviewResponseDTO.getClientId(),
                reviewResponseDTO.isPenalty(),
                reviewResponseDTO.isNoShow(),
                reviewResponseDTO.getReservationId(),
                reviewResponseDTO.getResponse()
        );
        vendorReviewRepository.save(vendorReview);
        vendorReviewRequestService.delete(reviewResponseDTO.getVendorReviewRequestId());
        Client client= clientService.getById(reviewResponseDTO.getClientId().toString());
        String penaltyVendorText = "";
        String penaltyClientText = "";
        if(reviewResponseDTO.isCheckPenalty() && reviewResponseDTO.isCheckNoShow())
        {
            client.setNumOfPenalties(client.getNumOfPenalties() + 2);
            penaltyVendorText = "Nalog klijenta koji je koristio jedan od vaših resursa će dobiti 2 penala.";
            penaltyClientText = "Administrator je odlučio da Vaš nalog dobije 2 penala za neprostojnost i odsutstvo";
        } else if (reviewResponseDTO.isCheckPenalty()) {
            client.setNumOfPenalties(client.getNumOfPenalties() + 1);
            penaltyVendorText = "Nalog klijenta koji je koristio jedan od vaših resursa će dobiti penal";
            penaltyClientText = "Administrator je odlučio da Vaš nalog dobije penal";
        } else if (reviewResponseDTO.isCheckNoShow()) {
            client.setNumOfPenalties(client.getNumOfPenalties() + 1);
            penaltyVendorText = "Nalog klijenta koji je koristio jedan od vaših resursa će dobiti penal jer se nije pojavio";
            penaltyClientText = "Administrator je odlučio da Vaš nalog dobije penal za odsutstvo";
        }
        String fullResponse= "Administratorov odgovor na recenziju: " + reviewResponseDTO.getResponse();
        String emailForVendor=emailService.buildHTMLEmail(list[1],fullResponse,penaltyVendorText,"Recenzija pružaoca usluga");
        emailService.send(list[3], emailForVendor, "Recenzija pružioca usluga");
        String emailForClient=emailService.buildHTMLEmail(client.getName(),fullResponse,penaltyClientText,"Recenzija pružaoca usluga");
        emailService.send(client.getEmail(), emailForClient, "Recenzija pružioca usluga");
        return "Uspešno ste obradili receniziju pružilaca usluga";
    }

    public String denyVendorReview(VendorRequestReviewDenialDTO denialDTO) {
        String[] list = getResourceTitleVendorNameVendorId(denialDTO.getResourceId());
        vendorReviewRequestService.delete(denialDTO.getVendorReviewRequestId());
        String fullResponse= "Administratorov odgovor na recenziju: " + denialDTO.getResponse();
        String emailForVendor=emailService.buildHTMLEmail(list[1],fullResponse,"Recenzija je odbijena","Recenzija pružaoca usluga");
        emailService.send(list[3], emailForVendor, "Recenzija pružioca usluga");
        return "Uspešno ste odbili recenziju";
    }
}
