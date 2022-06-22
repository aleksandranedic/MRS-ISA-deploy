package com.project.team9.service;

import com.project.team9.dto.ClientReviewDTO;
import com.project.team9.dto.ReviewScoresDTO;
import com.project.team9.dto.UserStatDTO;
import com.project.team9.model.request.VendorReviewRequest;
import com.project.team9.model.review.ClientReview;
import com.project.team9.model.review.VendorReview;
import com.project.team9.model.user.Client;
import com.project.team9.repo.ClientReviewRepository;
import com.project.team9.repo.VendorReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClientReviewService {
    private final ClientReviewRepository repository;
    private final VendorReviewRequestService vendorReviewRequestService;
    private final VendorReviewRepository vendorReviewRepository;
    private final ClientService clientService;
    private final UserCategoryService userCategoryService;


    @Autowired
    public ClientReviewService(ClientReviewRepository repository, ClientService clientService, VendorReviewRequestService vendorReviewRequestService, VendorReviewRepository vendorReviewRepository, ClientService clientService1, UserCategoryService userCategoryService) {
        this.repository = repository;

        this.vendorReviewRequestService = vendorReviewRequestService;
        this.vendorReviewRepository = vendorReviewRepository;

        this.clientService = clientService1;
        this.userCategoryService = userCategoryService;
    }

    public List<ClientReviewDTO> getResourceReviews(Long resource_id) {
        List<ClientReview> reviews = repository.findByResourceId(resource_id);
        List<ClientReviewDTO> reviewsDTO = new ArrayList<ClientReviewDTO>();
        for (ClientReview review : reviews) {
            reviewsDTO.add(getDTOFromClientReview(review));
        }
        return reviewsDTO;
    }
    public List<ClientReviewDTO> getVendorReviews(Long vendor_id) {
        List<ClientReview> reviews = repository.findByVendorId(vendor_id);
        List<ClientReviewDTO> reviewsDTO = new ArrayList<ClientReviewDTO>();
        for (ClientReview review : reviews) {
            reviewsDTO.add(getDTOFromClientReview(review));
        }
        return reviewsDTO;
    }

    private ClientReviewDTO getDTOFromClientReview(ClientReview review) {
        Client client = clientService.getById(String.valueOf(review.getClientId()));
        String name = client.getFirstName() + " " + client.getLastName();
        return new ClientReviewDTO(review.getId(), client.getProfileImg().getPath(), name, review.getRating(), review.getText(), client.getId());
    }

    public ReviewScoresDTO getReviewScores(Long id, String type) {
        List<ClientReviewDTO> allReviews;
        if (type.equals("resource")){
            allReviews = this.getResourceReviews(id);
        }
        else{
            allReviews = this.getVendorReviews(id);
        }
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("five", 0);
        scores.put("four", 0);
        scores.put("three", 0);
        scores.put("two", 0);
        scores.put("one", 0);
        for (ClientReviewDTO reviewDTO : allReviews) {
            if (reviewDTO.getRating() == 5) {
                scores.merge("five", 1, Integer::sum);
            }
            if (reviewDTO.getRating() == 4) {
                scores.merge("four", 1, Integer::sum);
            }
            if (reviewDTO.getRating() == 3) {
                scores.merge("three", 1, Integer::sum);
            }
            if (reviewDTO.getRating() == 2) {
                scores.merge("two", 1, Integer::sum);
            }
            if (reviewDTO.getRating() == 1) {
                scores.merge("one", 1, Integer::sum);
            }
        }
        int num = scores.get("five") + scores.get("four") + scores.get("three") + scores.get("two") + scores.get("one");
        double scale = Math.pow(10, 1);
        double fives = (double) scores.get("five") / num * 100;
        fives = Math.round(fives * scale) / scale;
        double fours = (double) scores.get("four") / num * 100;
        fours = Math.round(fours * scale) / scale;
        double threes = (double) scores.get("three") / num * 100;
        threes = Math.round(threes * scale) / scale;
        double twos = (double) scores.get("two") / num * 100;
        twos = Math.round(twos * scale) / scale;
        double ones = (double) scores.get("one") / num * 100;
        ones = Math.round(ones * scale) / scale;
        return new ReviewScoresDTO(fives, fours, threes, twos, ones);
    }

    public double getRating(Long id, String type) {
        ReviewScoresDTO reviews = getReviewScores(id, type);
        double sum = reviews.getFiveStars() * 5 + reviews.getFourStars() * 4 + reviews.getThreeStars() * 3 + reviews.getTwoStars() * 2 + reviews.getOneStars();
        double num = reviews.getFiveStars() + reviews.getFourStars() + reviews.getThreeStars() + reviews.getTwoStars() + reviews.getOneStars();
        double result = sum / num;
        double scale = Math.pow(10, 1);
        return Math.round(result * scale) / scale;
    }
    public boolean clientHasReview(Long resourceId, Long clientId) {
        return repository.findReviewForClientAndResource(resourceId, clientId).size() > 0;
    }

    public Long sendVendorReviewRequest(VendorReview vendorReview) {
        return vendorReviewRequestService.addVendorReviewRequest(new VendorReviewRequest(vendorReview));
    }

    public boolean reservationHasReview(Long id) {
        return vendorReviewRequestService.reservationHasReviewRequest(id) || hasReview(id);
    }

    private boolean hasReview(Long id) {
        return vendorReviewRepository.findReviewForReservation(id).size() > 0;
    }

    public void saveClientReview(ClientReview clientReview){
        repository.save(clientReview);
    }


    public UserStatDTO getUserStat(Long id) {
        Client client = clientService.getById(String.valueOf(id));
        return new UserStatDTO(
                client.getNumOfPenalties(),
                client.getNumOfPoints(),
                userCategoryService.getClientCategoryBasedOnPoints(client.getNumOfPoints()),
                this.getRating(id, "client")
        );
    }
}
