package com.project.team9.service;

import com.project.team9.dto.*;

import com.project.team9.exceptions.CannotDeleteException;
import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.reservation.AdventureReservation;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.reservation.BoatReservation;
import com.project.team9.model.reservation.VacationHouseReservation;
import com.project.team9.model.user.Client;
import com.project.team9.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";
    final String IMAGES_PATH = "/images/clients/";
    private final ClientRepository clientRepository;
    private final ImageService imageService;
    private final AdventureReservationService adventureReservationService;
    private final BoatReservationService boatReservationService;
    private final VacationHouseReservationService vacationHouseReservationService;
    private final AddressService addressService;
    private final ReservationService reservationService;

    @Autowired
    public ClientService(ClientRepository clientRepository, ImageService imageService, AdventureReservationService adventureReservationService, BoatReservationService boatReservationService, VacationHouseReservationService vacationHouseReservationService, AddressService addressService, ReservationService reservationService) {
        this.clientRepository = clientRepository;
        this.imageService = imageService;
        this.adventureReservationService = adventureReservationService;
        this.boatReservationService = boatReservationService;
        this.vacationHouseReservationService = vacationHouseReservationService;
        this.addressService = addressService;
        this.reservationService = reservationService;
    }

    public List<Client> getClients() {
        return clientRepository.findAll().stream().filter(client -> !client.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public Client getById(String id) {
        return clientRepository.getById(Long.parseLong(id));
    }

    public void deleteById(Long id) {
        clientRepository.findById(id).ifPresent(client -> {
            client.setDeleted(true);
            addClient(client);
        });

    }

    public Boolean changeProfilePicture(String id, MultipartFile multipartFile) throws IOException {
        try {
            Client client = this.getById(id);
            String path = saveImage(client, multipartFile);
            Image image = getImage(path);
            client.setProfileImg(image);
            this.addClient(client);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Image getImage(String path) {
        Image image;
        Optional<Image> optImg = imageService.getImageByPath(path);
        image = optImg.orElseGet(() -> new Image(path));
        imageService.save(image);
        return image;
    }

    private String saveImage(Client client, MultipartFile multipartFile) throws IOException {
        String pathStr = "";
        if (multipartFile == null) {
            return pathStr;
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + client.getId());
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + client.getId());
        savePictureOnPath(client, multipartFile, pathStr, path);
        pathStr = savePictureOnPath(client, multipartFile, pathStr, path_target);
        return pathStr;
    }

    private String savePictureOnPath(Client client, MultipartFile mpf, String pathStr, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String fileName = mpf.getOriginalFilename();
        try (InputStream inputStream = mpf.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            pathStr = IMAGES_PATH + client.getId() + "/" + fileName;
        } catch (DirectoryNotEmptyException dnee) {
            throw new IOException("Directory Not Empty Exception: " + fileName, dnee);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        return pathStr;
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public Client getClientByEmailAndPassword(LoginDTO loginDTO) {
        for (Client client : getClients()) {
            if (client.getPassword().equals(loginDTO.getPassword()) && client.getEmail().equals(loginDTO.getUsername())) {
                return client;
            }
        }
        return null;
    }

    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public List<ReservationDTO> getReservations(Long id) {
        List<ReservationDTO> reservations = new ArrayList<>();
        for (AdventureReservation reservation : adventureReservationService.getAdventureReservationsForClientId(id)) {
            reservations.add(new ClientReservationDTO(reservation.getAppointments(), reservation.getNumberOfClients(), reservation.getAdditionalServices(), reservation.getPrice(), reservation.getClient(), reservation.getResource().getTitle(), reservation.isBusyPeriod(), reservation.isQuickReservation(), reservation.getResource().getId(), reservation.getId(), "adventure"));
        }
        for (BoatReservation reservation : boatReservationService.getBoatReservationsForClientId(id)) {
            reservations.add(new ClientReservationDTO(reservation.getAppointments(), reservation.getNumberOfClients(), reservation.getAdditionalServices(), reservation.getPrice(), reservation.getClient(), reservation.getResource().getTitle(), reservation.isBusyPeriod(), reservation.isQuickReservation(), reservation.getResource().getId(), reservation.getId(), "boat"));
        }
        for (VacationHouseReservation reservation : vacationHouseReservationService.getVacationHouseReservationsForClienId(id)) {
            reservations.add(new ClientReservationDTO(reservation.getAppointments(), reservation.getNumberOfClients(), reservation.getAdditionalServices(), reservation.getPrice(), reservation.getClient(), reservation.getResource().getTitle(), reservation.isBusyPeriod(), reservation.isQuickReservation(), reservation.getResource().getId(), reservation.getId(), "house"));
        }
        return reservations;
    }

    public Client updateLoggedUser(String id, ClientDTO clientDTO) {
        Client currentClient = getById(id);
        currentClient.setFirstName(clientDTO.getFirstName());
        currentClient.setLastName(clientDTO.getLastName());
        currentClient.setPhoneNumber(clientDTO.getPhoneNumber());
        Address address = addressService.getByAttributes(clientDTO.getAddress());
        if (address == null) {
            address = new Address();
            address.setStreet(clientDTO.getAddress().getStreet());
            address.setNumber(clientDTO.getAddress().getNumber());
            address.setCountry(clientDTO.getAddress().getCountry());
            address.setPlace(clientDTO.getAddress().getPlace());
            addressService.addAddress(address);
        }
        currentClient.setAddress(address);
        addClient(currentClient);
        return currentClient;
    }

    public Long delete(Long id) throws CannotDeleteException {
        Client client = clientRepository.getById(id);
        client.setDeleted(true);
        for (AdventureReservation r : adventureReservationService.getAdventureReservationsForClientId(id)) {
            if (r.getAppointments().get(r.getAppointments().size() - 1).getEndTime().isAfter(LocalDateTime.now())) {
                throw new CannotDeleteException();
            }
        }
        for (BoatReservation r : boatReservationService.getBoatReservationsForClientId(id)) {
            if (r.getAppointments().get(r.getAppointments().size() - 1).getEndTime().isAfter(LocalDateTime.now())) {
                throw new CannotDeleteException();
            }
        }
        for (VacationHouseReservation r : vacationHouseReservationService.getVacationHouseReservationsForClienId(id)) {
            if (r.getAppointments().get(r.getAppointments().size() - 1).getEndTime().isAfter(LocalDateTime.now())) {
                throw new CannotDeleteException();
            }
        }
        return clientRepository.save(client).getId();
    }

    public String canReserve(NewReservationDTO dto) {

        Client client = clientRepository.getById(dto.getClientId());
        if (client.getNumOfPenalties() >= 3) {
            return "Klijent ima 3 ili više penala.";
        }

        List<Appointment> appointments = new ArrayList<Appointment>();

        LocalDateTime startTime = LocalDateTime.of(dto.getStartYear(), Month.of(dto.getStartMonth()), dto.getStartDay(), dto.getStartHour(), dto.getStartMinute());
        LocalDateTime endTime = startTime.plusHours(1);

        LocalDateTime finalTime = LocalDateTime.of(dto.getEndYear(), Month.of(dto.getEndMonth()), dto.getEndDay(), dto.getEndHour(), dto.getEndMinute());

        while (endTime.isBefore(finalTime)) {
            appointments.add(new Appointment(startTime, endTime));
            startTime = endTime;
            endTime = startTime.plusHours(1);
        }

        appointments.add(new Appointment(startTime, finalTime));

        try {
            for (AdventureReservation r : adventureReservationService.getPossibleCollisionReservationsForClient(dto.getClientId(), dto.getResourceId())) {
                for (Appointment a : r.getAppointments()) {
                    for (Appointment newAppointment : appointments) {
                        reservationService.checkAppointmentCollision(a, newAppointment);
                    }
                }
            }
            for (VacationHouseReservation r : vacationHouseReservationService.getPossibleCollisionReservationsForClient(dto.getClientId(), dto.getResourceId())) {
                for (Appointment a : r.getAppointments()) {
                    for (Appointment newAppointment : appointments) {
                        reservationService.checkAppointmentCollision(a, newAppointment);
                    }
                }
            }
            for (BoatReservation r : boatReservationService.getPossibleCollisionReservationsForClient(dto.getClientId(), dto.getResourceId())) {
                for (Appointment a : r.getAppointments()) {
                    for (Appointment newAppointment : appointments) {
                        reservationService.checkAppointmentCollision(a, newAppointment);
                    }
                }
            }

        } catch (ReservationNotAvailableException e) {
            return "Termin koji ste pokušali da zauzmete nije dostupan. Klijent već ima rezervaciju u tom terminu ili ste pokušali da napravite rezervaciju u terminu koju je klijent već otkazao za ovaj entitet.";
        }
        return "Ok";
    }
}
