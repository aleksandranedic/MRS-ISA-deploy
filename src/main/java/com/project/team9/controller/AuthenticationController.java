package com.project.team9.controller;

import com.project.team9.dto.LoginDTO;
import com.project.team9.dto.LoginResponseDTO;
import com.project.team9.dto.PasswordsDTO;
import com.project.team9.dto.UserDTO;
import com.project.team9.model.user.Administrator;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.User;
import com.project.team9.model.user.UserTokenState;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final TokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceSecurity userServiceSecurity;

    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder, TokenUtils tokenUtils, AuthenticationManager authenticationManager, UserServiceSecurity userServiceSecurity) {
        this.tokenUtils = tokenUtils;
        this.userServiceSecurity = userServiceSecurity;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(
            @RequestBody LoginDTO loginDTO) {

//        User user=clientRepository.findByEmail(loginDTO.getUsername());
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
            // kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Kreiraj token za tog korisnika
            User user = (User) authentication.getPrincipal();
            if (user.getDeleted()) {
                return ResponseEntity.ok(new LoginResponseDTO("Korisnik je obrisan", null, null, true));
            }
            if (!user.isEnabled()) {
                return ResponseEntity.ok(new LoginResponseDTO("Korisnik nije verifikovao svoj nalog", null, null, true));
            }
            String jwt = tokenUtils.generateToken(user.getUsername());

            // Vrati token kao odgovor na uspesnu autentifikaciju
            LoginResponseDTO body = new LoginResponseDTO(jwt, user.getRoleName(), user.getId(), user.isConfirmed());

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.ok(new LoginResponseDTO("Neuspešna prijava, probajte ponovo", null, null, true));
        }
    }
//    @PostMapping(value = "/refresh")
//    public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {
//        String token = tokenUtils.getToken(request);
//        String username = this.tokenUtils.getUsernameFromToken(token);
//        User user = (User) this.userServiceSecurity.loadUserByUsername(username);
//
//        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
//            String refreshedToken = tokenUtils.refreshToken(token);
//            int expiresIn = tokenUtils.getExpiredIn();
//
//            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
//        } else {
//            UserTokenState userTokenState = new UserTokenState();
//            return ResponseEntity.badRequest().body(userTokenState);
//        }
//    }


    @PostMapping(value ="/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestBody PasswordsDTO passwordsDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken)
                return new ResponseEntity<>("Neuspešno. Ne postoji ulogovani korisnik", HttpStatus.NOT_FOUND);
            User user = (User) authentication.getPrincipal();
            if (user == null)
                return new ResponseEntity<>("Neuspešno. Ne postoji ulogovani korisnik", HttpStatus.EXPECTATION_FAILED);
            if (!passwordEncoder.bCryptPasswordEncoder().matches(passwordsDTO.getOldPassword(), user.getPassword()))
                return new ResponseEntity<>("Neuspešno. Stara šifra i uneta stara šifra se ne poklapaju", HttpStatus.EXPECTATION_FAILED);
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
                Administrator administrator = (Administrator) user;
                administrator.setConfirmed(true);
                userServiceSecurity.addAdmin(administrator);
            }
            String jwt = tokenUtils.generateToken(user.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Korisnik nije pronadjen");
        }

    }

    @GetMapping("/getLoggedUser")
    public ResponseEntity<UserDTO> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = (User) authentication.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
    }


}