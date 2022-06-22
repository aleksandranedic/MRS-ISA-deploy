package com.project.team9.service;

import com.project.team9.dto.ClientReviewRequestDTO;
import com.project.team9.dto.ClientReviewResponseDTO;
import com.project.team9.model.request.ClientReviewRequest;
import com.project.team9.model.review.ClientReview;
import com.project.team9.model.user.Client;
import com.project.team9.repo.ClientReviewRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientReviewRequestService {
    private final ClientReviewRequestRepository repository;
    private final AdventureService adventureService;
    private final BoatService boatService;
    private final VacationHouseService vacationHouseService;
    private final ClientService clientService;
    private final FishingInstructorService fishingInstructorService;
    private final BoatOwnerService boatOwnerService;
    private final VacationHouseOwnerService vacationHouseOwnerService;
    private final ClientReviewService clientReviewService;
    private final EmailService emailService;

    @Autowired
    public ClientReviewRequestService(ClientReviewRequestRepository repository, AdventureService adventureService, BoatService boatService, VacationHouseService vacationHouseService, ClientService clientService, FishingInstructorService fishingInstructorService, BoatOwnerService boatOwnerService, VacationHouseOwnerService vacationHouseOwnerService, ClientReviewService clientReviewService, EmailService emailService) {
        this.repository = repository;
        this.adventureService = adventureService;
        this.boatService = boatService;
        this.vacationHouseService = vacationHouseService;
        this.clientService = clientService;
        this.fishingInstructorService = fishingInstructorService;
        this.boatOwnerService = boatOwnerService;
        this.vacationHouseOwnerService = vacationHouseOwnerService;
        this.clientReviewService = clientReviewService;
        this.emailService = emailService;
    }

    public Long sendReviewRequest(ClientReview review) {
        return repository.save(new ClientReviewRequest(review)).getId();
    }

    public boolean hasReviewRequests(Long resourceId, Long clientId) {
        return repository.findReviewRequests(resourceId, clientId).size() > 0;
    }

    public List<ClientReviewRequest> getAllClientRequestReviews() {
        return repository.findAll().stream().filter(clientReviewRequest -> !clientReviewRequest.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<ClientReviewRequestDTO> getAllClientReviewRequests() {
        List<ClientReviewRequestDTO> reviewRequests = new ArrayList<>();
        for (ClientReviewRequest clientReviewRequest : getAllClientRequestReviews()) {
            String resourceTitle = clientReviewRequest.getResourceId() == null ? "" : getResourceTitle(clientReviewRequest.getResourceId());
            String vendorFullName = clientReviewRequest.getVendorId() == null ? "" : getVendorFullName(clientReviewRequest.getVendorId());
            reviewRequests.add(new ClientReviewRequestDTO(
                    resourceTitle,
                    vendorFullName,
                    clientReviewRequest.getRating(),
                    clientService.getById(String.valueOf(clientReviewRequest.getClientId())).getName(),
                    clientReviewRequest.getComment(),
                    clientReviewRequest.getResourceId(),
                    clientReviewRequest.getClientId(),
                    clientReviewRequest.getVendorId(),
                    clientReviewRequest.getId()
            ));
        }
        return reviewRequests;
    }

    private String getVendorFullName(Long vendorId) {
        String name = "";
        try {
            name = fishingInstructorService.getById(vendorId.toString()).getName();
        } catch (Exception e) {
            name = "";
        }
        if (name.equals("")) {
            try {
                name = boatOwnerService.getOwner(vendorId).getName();
            } catch (Exception e) {
                name = "";
            }
        }
        if (name.equals("")) {
            try {
                name = vacationHouseOwnerService.getOwner(vendorId).getName();
            } catch (Exception e) {
                name = "";
            }
        }
        return name;
    }

    private String getVendorEmail(Long vendorId) {
        String email = "";
        try {
            email = fishingInstructorService.getById(vendorId.toString()).getEmail();
        } catch (Exception e) {
            email = "";
        }
        if (email.equals("")) {
            try {
                email = boatOwnerService.getOwner(vendorId).getEmail();
            } catch (Exception e) {
                email = "";
            }
        }
        if (email.equals("")) {
            try {
                email = vacationHouseOwnerService.getOwner(vendorId).getEmail();
            } catch (Exception e) {
                email = "";
            }
        }
        return email;
    }

    private String getResourceTitle(Long resourceId) {
        String title = "";
        try {
            title = adventureService.getById(resourceId.toString()).getTitle();
        } catch (Exception e) {
            title = "";
        }
        if (title.equals("")) {
            try {
                title = boatService.getBoat(resourceId).getTitle();
            } catch (Exception e) {
                title = "";
            }
        }
        if (title.equals("")) {
            try {
                title = vacationHouseService.getVacationHouse(resourceId).getTitle();
            } catch (Exception e) {
                title = "";
            }
        }
        return title;
    }

    private Long getResourceOwnerId(Long resourceId) {
        Long id = (long) -1;
        try {
            id = adventureService.getById(resourceId.toString()).getOwner().getId();
        } catch (Exception e) {
            id = (long) -1;
        }
        if (id == -1) {
            try {
                id = boatService.getBoat(resourceId).getOwner().getId();
            } catch (Exception e) {
                id = (long) -1;
            }
        }
        if (id == -1) {
            try {
                id = vacationHouseService.getVacationHouse(resourceId).getOwner().getId();
            } catch (Exception e) {
                id = (long) -1;
            }
        }
        return id;
    }

    public String approveClientReview(ClientReviewResponseDTO clientReviewResponseDTO) {
        ClientReview clientReview = new ClientReview(
                clientReviewResponseDTO.getResourceId(),
                clientReviewResponseDTO.getVendorId(),
                clientReviewResponseDTO.getRating(),
                clientReviewResponseDTO.getText(),
                clientReviewResponseDTO.getClientId(),
                clientReviewResponseDTO.getResponse()
        );
        deleteClientReviewRequest(clientReviewResponseDTO.getRequestId());
        clientReviewService.saveClientReview(clientReview);
        Client client = clientService.getById(String.valueOf(clientReviewResponseDTO.getClientId()));
        String vendorEmail="";
        String vendorName="";
        if (clientReviewResponseDTO.getVendorId() == null) {
            vendorEmail = getVendorEmail(getResourceOwnerId(clientReviewResponseDTO.getResourceId()));
            vendorName = getVendorFullName(getResourceOwnerId(clientReviewResponseDTO.getResourceId()));
        } else if (clientReviewResponseDTO.getResourceId() == null) {
            vendorEmail = getVendorEmail(clientReviewResponseDTO.getVendorId());
            vendorName = getVendorFullName(clientReviewResponseDTO.getVendorId());
        }
        String fullResponse="Administratoreva poruka je: "+clientReviewResponseDTO.getResponse();
        String email=emailService.buildHTMLEmail(vendorName,fullResponse,"Zahtev je prihvaćen", "Recenzija");
        emailService.send(vendorEmail, email, "Recenzija");
        email=emailService.buildHTMLEmail(client.getName(), fullResponse,"Zahtev je prihvaćen", "Recenzija");
        emailService.send(client.getEmail(), email, "Recenzija");
        return "Uspešno ste odobrili recenizju klijenta";
    }


    public void deleteClientReviewRequest(Long id) {
        ClientReviewRequest clientReviewRequest = repository.getById(id);
        clientReviewRequest.setDeleted(true);
        repository.save(clientReviewRequest);
    }

    public String denyClientReview(ClientReviewResponseDTO clientReviewResponseDTO) {
        deleteClientReviewRequest(clientReviewResponseDTO.getRequestId());
        Client client = clientService.getById(String.valueOf(clientReviewResponseDTO.getClientId()));
        String fullResponse="Administratoreva poruka je: "+clientReviewResponseDTO.getResponse();
        String email=emailService.buildHTMLEmail(client.getName(), fullResponse,"Zahtev je odbijen", "Recenzija");
        emailService.send(client.getEmail(), email, "Recenzija");
        return "Uspešno ste odbili recenziju";
    }
}
