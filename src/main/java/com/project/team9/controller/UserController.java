package com.project.team9.controller;

import com.project.team9.dto.PasswordsDTO;
import com.project.team9.model.user.Administrator;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.User;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.security.PasswordEncoder;
import com.project.team9.security.auth.TokenUtils;
import com.project.team9.service.UserServiceSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserServiceSecurity userServiceSecurity;

    @PostMapping(value ="/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestBody PasswordsDTO passwordsDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken)
                return new ResponseEntity<>("Neuspešno. Ne postoji ulogovani korisnik", HttpStatus.NOT_FOUND);
            User user = (User) authentication.getPrincipal();
            if (user == null)
                return new ResponseEntity<>("Neuspešno. Ne postoji ulogovani korisnik", HttpStatus.UNAUTHORIZED);
            if (!passwordEncoder.bCryptPasswordEncoder().matches(passwordsDTO.getOldPassword(), user.getPassword()))
                return new ResponseEntity<>("Neuspešno. Stara šifra i uneta stara šifra se ne poklapaju", HttpStatus.UNAUTHORIZED);
            user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(passwordsDTO.getNewPassword()));
            user.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now()));
            if (user instanceof Client) {
                userServiceSecurity.addClient((Client) user);
            } else if (user instanceof FishingInstructor) {
                userServiceSecurity.addFishingInstructor((FishingInstructor) user);
            } else if (user instanceof VacationHouseOwner) {
                userServiceSecurity.addVacationHouseOwner((VacationHouseOwner) user);
            } else if (user instanceof BoatOwner) {
                userServiceSecurity.addBoatOwner((BoatOwner) user);
            } else if (user instanceof Administrator) {
                userServiceSecurity.addAdmin((Administrator) user);
            }
            String jwt = tokenUtils.generateToken(user.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Korisnik nije pronadjen");
        }

    }
}
