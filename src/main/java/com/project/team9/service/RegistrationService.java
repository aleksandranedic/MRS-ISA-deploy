package com.project.team9.service;

import com.project.team9.model.Image;
import com.project.team9.model.request.RegistrationRequest;
import com.project.team9.model.user.Administrator;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.Role;
import com.project.team9.model.user.User;
import com.project.team9.security.token.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class RegistrationService {

    private final UserServiceSecurity userServiceSecurity;
    private final RegistrationRequestService registrationRequestService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final RoleService roleService;
    private final ImageService imageService;

    @Value("${frontendlink}")
    private String frontLink;

    @Autowired
    public RegistrationService(UserServiceSecurity userServiceSecurity, RegistrationRequestService registrationRequestService, EmailService emailService, ConfirmationTokenService confirmationTokenService, RoleService roleService, ImageService imageService) {
        this.userServiceSecurity = userServiceSecurity;
        this.registrationRequestService = registrationRequestService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.roleService = roleService;
        this.imageService = imageService;
    }

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

    public String register(RegistrationRequest registrationRequest) {

        Role role = roleService.findRoleByName(registrationRequest.getUserRole());
        String response = "";
        if (role == null) {
            role = new Role(registrationRequest.getUserRole());
            roleService.save(role);
        }

        switch (registrationRequest.getUserRole()) {
            case "ADMINISTRATOR":
                String password = String.valueOf(generatePassword(12));
                Administrator administrator = new Administrator(
                        password,
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPhoneNumber(),
                        registrationRequest.getPlace(),
                        registrationRequest.getNumber(),
                        registrationRequest.getStreet(),
                        registrationRequest.getCountry(),
                        Boolean.FALSE, role
                );
                administrator.setConfirmed(false);
                Image image=new Image();
                image.setPath(null);
                imageService.save(image);
                administrator.setProfileImg(image);
                String emailMessage=userServiceSecurity.signUpAdmin(administrator);
                String email=emailService.buildHTMLEmail(administrator.getName(),emailMessage,"","Registracija na sajt Savana");
                emailService.send(administrator.getEmail(),email,"Registracija na sajt Savana");
                response = "Uspešno ste registrovali novog administratora.";
                break;
            case "CLIENT":
                Client user = new Client(
                        registrationRequest.getPassword(),
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPhoneNumber(),
                        registrationRequest.getPlace(),
                        registrationRequest.getNumber(),
                        registrationRequest.getStreet(),
                        registrationRequest.getCountry(),
                        Boolean.FALSE, role);
                String token = userServiceSecurity.signUpUser(user);
                String link = "<a href=\""+this.frontLink+"confirmedEmail/" + token+"\">Aktivirajte</a>";
                email=emailService.buildHTMLEmail(user.getName(),"Hvala na registraciji. Molim Vas kliknite link ispod da bi aktivirali svoj nalog:", link ,"Verifikacija emaila");
                emailService.send(user.getEmail(), email, "Verifikacija emaila");
                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                );
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                response = "Uspešno ste izvršili registraciju.\nProverite email kako biste verifikovali svoj nalog";
                break;
            case "FISHING_INSTRUCTOR":
            case "BOAT_OWNER":
            case "VACATION_HOUSE_OWNER":
                registrationRequestService.addRegistrationRequest(registrationRequest);
                response = "Uspešno ste poslali zahtev o registraciji";
                break;
        }
        return response;
    }


    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElse(null);
        if (confirmationToken == null)
            return "Token ne postoji";
        if (confirmationToken.getConfirmedAt() != null) {
            return "Vaš email je vec verifikovan";
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return "Vaš verifikacioni token je istekao";
        }
        confirmationTokenService.setConfirmedAt(token);
        User user = (User) userServiceSecurity.loadUserByUsername(confirmationToken.getUser().getEmail());
        user.setEnabled(true);
        userServiceSecurity.addClient((Client) user);

        return "Vaša verifikacija je uspesna";
    }
}
