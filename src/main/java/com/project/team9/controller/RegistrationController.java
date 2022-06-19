package com.project.team9.controller;

import com.project.team9.dto.VerificationDTO;
import com.project.team9.model.request.RegistrationRequest;
import com.project.team9.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "registration")
@CrossOrigin("*")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public String register(@RequestBody RegistrationRequest registrationRequest) {

        return registrationService.register(registrationRequest);
    }

    @GetMapping(path = "/confirm/{token}")
    public ResponseEntity<VerificationDTO> confirm(@PathVariable("token") String token) {
        String response = registrationService.confirmToken(token);
        VerificationDTO verificationDTO = new VerificationDTO();
        if (response.equals("Vasa verifikacija je uspesna")) {
            verificationDTO.setConfirmed(true);
            verificationDTO.setMessage("Vaš nalog je verifikovan. Možete se ulogavati na naš sajt");
            verificationDTO.setMessageTitle("Vaša verifikacija je uspešna");
        } else {
            verificationDTO.setConfirmed(false);
            verificationDTO.setMessage("Vaš nalog nije verifikovan. Molim vas pokušajte ponovo.");
            verificationDTO.setMessageTitle(response);
        }
        return new ResponseEntity<>(verificationDTO, HttpStatus.OK);
    }
}
