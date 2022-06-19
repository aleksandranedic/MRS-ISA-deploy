package com.project.team9.service;

import com.project.team9.model.Address;
import com.project.team9.model.user.Administrator;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.security.PasswordEncoder;
import com.project.team9.security.token.ConfirmationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceSecurity implements UserDetailsService {

    private final ClientService clientService;
    private final FishingInstructorService fishingInstructorService;
    private final VacationHouseOwnerService vacationHouseOwnerService;
    private final BoatOwnerService boatOwnerService;
    private final AddressService addressService;
    private final AdministratorService administratorService;
    private final ConfirmationTokenService confirmationTokenService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceSecurity(ClientService clientService, FishingInstructorService fishingInstructorService, VacationHouseOwnerService vacationHouseOwnerService, BoatOwnerService boatOwnerService, AddressService addressService, AdministratorService administratorService, ConfirmationTokenService confirmationTokenService, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.fishingInstructorService = fishingInstructorService;
        this.vacationHouseOwnerService = vacationHouseOwnerService;
        this.boatOwnerService = boatOwnerService;
        this.addressService = addressService;
        this.administratorService = administratorService;
        this.confirmationTokenService = confirmationTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (clientService.getClientByEmail(username) != null) {
            return clientService.getClientByEmail(username);
        } else if (fishingInstructorService.getFishingInstructorByEmail(username) != null) {
            return fishingInstructorService.getFishingInstructorByEmail(username);
        } else if (vacationHouseOwnerService.getVacationHouseOwnerByEmail(username) != null) {
            return vacationHouseOwnerService.getVacationHouseOwnerByEmail(username);
        } else if (administratorService.getAdministratorByEmail(username) != null) {
            return administratorService.getAdministratorByEmail(username);
        }else if (boatOwnerService.getBoatOwnerByEmail(username) != null) {
            return boatOwnerService.getBoatOwnerByEmail(username);
        } else if (administratorService.getAdministratorByEmail(username) != null) {
            return administratorService.getAdministratorByEmail(username);
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }

    public String signUpUser(Client user) {
        if (clientService.getClientByEmail(user.getUsername()) != null)
            return "korisnik vec postoji";

        String encodedPassword=passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        Address address = addressService.getByAttributes(user.getAddress());
        if (address == null) {
            addressService.addAddress(user.getAddress());
        } else {
            user.setAddress(address);
        }
        clientService.addClient(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
    public String signUpAdmin(Administrator administrator){
        if (administratorService.getAdministratorByEmail(administrator.getEmail()) != null)
            return "korisnik vec postoji";

        String password=administrator.getPassword();
        String encodedPassword=passwordEncoder.bCryptPasswordEncoder().encode(administrator.getPassword());
        administrator.setPassword(encodedPassword);
        Address address = addressService.getByAttributes(administrator.getAddress());
        if (address == null) {
            addressService.addAddress(administrator.getAddress());
        } else {
            administrator.setAddress(address);
        }
        administratorService.addAdmin(administrator);
        return "Registrovani ste na sajt Savana kao administrator. Vaša prijavna loznika je "+password+". Pri prvom prijavljivanju promenite lozinku.";
    }
    public void addFishingInstructor(FishingInstructor fishingInstructor){
        fishingInstructorService.addFishingInstructor(fishingInstructor);
    }

    public void addBoatOwner(BoatOwner boatOwner){
        boatOwnerService.save(boatOwner);
    }

    public void addVacationHouseOwner(VacationHouseOwner vacationHouseOwner){
        vacationHouseOwnerService.addOwner(vacationHouseOwner);
    }

    public void addClient(Client client){
        clientService.addClient(client);
    }

    public void addAdmin(Administrator administrator){ administratorService.addAdmin(administrator);}
}
