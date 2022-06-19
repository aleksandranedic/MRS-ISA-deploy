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
        if (clientReviewResponseDTO.getVendorId() != null) {
            vendorEmail = getVendorEmail(clientReviewResponseDTO.getVendorId());
            vendorName = getVendorFullName(clientReviewResponseDTO.getVendorId());
        } else if (clientReviewResponseDTO.getResourceId() != null) {
            vendorEmail = getVendorEmail(getResourceOwnerId(clientReviewResponseDTO.getResourceId()));
            vendorName = getVendorFullName(getResourceOwnerId(clientReviewResponseDTO.getResourceId()));
        }
        emailService.send(vendorEmail, buildEmail(vendorName, clientReviewResponseDTO.getResponse()), "Recenzija");
        emailService.send(client.getEmail(), buildEmail(client.getName(), clientReviewResponseDTO.getResponse()), "Recenzija");
        return "Uspešno ste odobrili recenizju klijenta";
    }

    private String buildEmail(String name, String response) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Obavestenje o receniziji</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Zdravo " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Ovo je administratorov odgovor na recenziju: " + response + " </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p></blockquote>\n " +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public void deleteClientReviewRequest(Long id) {
        ClientReviewRequest clientReviewRequest = repository.getById(id);
        clientReviewRequest.setDeleted(true);
        repository.save(clientReviewRequest);
    }

    public String denyClientReview(ClientReviewResponseDTO clientReviewResponseDTO) {
        deleteClientReviewRequest(clientReviewResponseDTO.getRequestId());
        Client client = clientService.getById(String.valueOf(clientReviewResponseDTO.getClientId()));
        emailService.send(client.getEmail(), buildEmail(client.getName(), clientReviewResponseDTO.getResponse()), "Recenzija");
        return "Uspešno ste odbili recenziju";
    }
}
