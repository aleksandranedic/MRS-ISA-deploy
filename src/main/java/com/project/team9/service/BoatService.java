package com.project.team9.service;

import com.project.team9.dto.*;
import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.Tag;
import com.project.team9.model.buissness.Pricelist;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.reservation.BoatReservation;
import com.project.team9.model.resource.Boat;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.repo.BoatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoatService {
    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";
    final String IMAGES_PATH = "/images/boats/";

    private final BoatRepository repository;
    private final AddressService addressService;
    private final PricelistService pricelistService;
    private final TagService tagService;
    private final ImageService imageService;
    private final BoatReservationService boatReservationService;
    private final BoatOwnerService boatOwnerService;
    private final AppointmentService appointmentService;
    private final ClientService clientService;
    private final ReservationService reservationService;
    private final ClientReviewService clientReviewService;
    private final EmailService emailService;
    private final PointlistService pointlistService;
    private final UserCategoryService userCategoryService;

    @Value("${frontendlink}")
    private String frontLink;

    @Autowired
    public BoatService(BoatRepository repository, AddressService addressService, PricelistService pricelistService, TagService tagService, ImageService imageService, BoatReservationService boatReservationService, BoatOwnerService boatOwnerService, AppointmentService appointmentService, ClientService clientService, ReservationService reservationService, ClientReviewService clientReviewService, EmailService emailService, PointlistService pointlistService, UserCategoryService userCategoryService) {
        this.repository = repository;
        this.addressService = addressService;
        this.pricelistService = pricelistService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.boatReservationService = boatReservationService;
        this.boatOwnerService = boatOwnerService;
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.reservationService = reservationService;
        this.clientReviewService = clientReviewService;
        this.emailService = emailService;
        this.pointlistService = pointlistService;
        this.userCategoryService = userCategoryService;
    }

    public List<Boat> getBoats() {
        return repository.findAll().stream().filter(boat -> !boat.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public BoatCardDTO getBoatCard(Long id) {
        Boat boat = getBoat(id);
        String address = boat.getAddress().getStreet() + " " + boat.getAddress().getNumber() + ", " + boat.getAddress().getPlace() + ", " + boat.getAddress().getCountry();
        return new BoatCardDTO(boat.getId(), boat.getImages().get(0).getPath(), boat.getTitle(), boat.getDescription(), address);
    }

    @Transactional(readOnly = false)
    public Boolean addQuickReservation(Long id, BoatQuickReservationDTO quickReservationDTO) throws ReservationNotAvailableException {
        Boat boat;
        try {
            boat = this.getByIdConcurrent(id);
        } catch (PessimisticLockingFailureException plfe) {
            return false;
        }
        BoatReservation reservation = getReservationFromDTO(quickReservationDTO, true);
        reservation.setResource(boat);

        List<BoatReservation> reservations = boatReservationService.getPossibleCollisionReservations(reservation.getResource().getId());
        for (BoatReservation r : reservations) {
            for (Appointment a : r.getAppointments()) {
                for (Appointment newAppointment : reservation.getAppointments()) {
                    reservationService.checkAppointmentCollision(a, newAppointment);
                }
            }
        }

        boatReservationService.addReservation(reservation);
        boat.addReservation(reservation);
        this.addBoat(boat);
        for (Long userId : boat.getSubClientUsernames()) {
            Client client = clientService.getById(String.valueOf(userId));
            String fullResponse = "Napravljena je akcija na koji ste se preplatili\n " +
                    "Avanture na brod kоšta " + reservation.getPrice() + "\n" +
                    "Zakazani period je od " + reservation.getAppointments().get(0).getStartTime().toString() + " do " +
                    reservation.getAppointments().get(reservation.getAppointments().size() - 1).getEndTime().toString();
            String additionalText = "<a href=\"" + frontLink + "\">Prijavite se i rezervišite je</a>";
            String emailForSubbedUser = emailService.buildHTMLEmail(client.getName(), fullResponse, additionalText, "Notifikacija o pretplacenim akcijama");
            emailService.send(client.getEmail(), emailForSubbedUser, "Notifikacija o pretplacenim akcijama");
        }
        return true;
    }

    private BoatReservation getReservationFromDTO(BoatQuickReservationDTO dto, Boolean isQuick) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        String[] splitDate = dto.getStartDate().split(" ");
        String[] splitTime = splitDate[3].split(":");
        Appointment startDateAppointment = Appointment.getHourAppointment(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[0]), Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
        appointments.add(startDateAppointment);
        appointmentService.save(startDateAppointment);
        Appointment currApp = startDateAppointment;
        for (int i = 0; i < dto.getDuration() - 1; i++) {
            LocalDateTime startDate = currApp.getEndTime();
            LocalDateTime endDate = startDate.plusHours(1);
            currApp = new Appointment(startDate, endDate);
            appointmentService.save(currApp);
            appointments.add(currApp);
        }
        List<Tag> tags = new ArrayList<Tag>();
        for (String tagText : dto.getTagsText()) {
            Tag tag = new Tag(tagText);
            tagService.addTag(tag);
            tags.add(tag);
        }
        BoatReservation reservation = new BoatReservation(dto.getNumberOfPeople(), dto.getPrice());
        reservation.setClient(null);
        reservation.setAdditionalServices(tags);
        reservation.setAppointments(appointments);
        reservation.setQuickReservation(isQuick);
        return reservation;
    }

    public Boolean updateQuickReservation(Long id, BoatQuickReservationDTO quickReservationDTO) {
        Boat boat = this.getBoat(id);
        BoatReservation originalReservation = boatReservationService.getBoatReservation(quickReservationDTO.getReservationID());
        if (!originalReservation.isQuickReservation())
            return false;
        BoatReservation newReservation = getReservationFromDTO(quickReservationDTO, true);
        updateQuickReservation(originalReservation, newReservation);
        try {
            boatReservationService.saveQuickReservationAsReservation(originalReservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            return false;
        }
        this.addBoat(boat);
        return true;
    }

    private void updateQuickReservation(BoatReservation originalReservation, BoatReservation newReservation) {
        originalReservation.setAppointments(newReservation.getAppointments());
        originalReservation.setAdditionalServices(newReservation.getAdditionalServices());
        originalReservation.setNumberOfClients(newReservation.getNumberOfClients());
        originalReservation.setPrice(newReservation.getPrice());
    }

    public List<ReservationDTO> getReservations(Long id) {
        Boat boat = this.getBoat(id);
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (BoatReservation boatReservation : boat.getReservations()) {
            if (!boatReservation.isQuickReservation() && !boatReservation.isBusyPeriod()) {
                reservations.add(createDTOFromReservation(boatReservation));
            }
        }
        return reservations;
    }

    public Boolean deleteQuickReservation(Long id, String reservationID) {
        Boat boat = this.getBoat(id);
        BoatReservation originalReservation = boatReservationService.getBoatReservation(Long.parseLong(reservationID));
        originalReservation.setDeleted(true);
        boatReservationService.save(originalReservation);
        this.addBoat(boat);
        return true;
    }

    public Long reserveQuickReservation(ReserveQuickReservationDTO dto) {
        BoatReservation quickReservation = boatReservationService.getBoatReservation(dto.getReservationID());
        if (!quickReservation.isQuickReservation())
            return Long.valueOf("-1");
        Boat boat = quickReservation.getResource();
        boat.removeQuickReservation(quickReservation);
        Client client = clientService.getById(dto.getClientID().toString());

        quickReservation.setClient(client);
        quickReservation.setQuickReservation(false);
        boat.addReservation(quickReservation);
        try {
            Long id = boatReservationService.saveQuickReservationAsReservation(quickReservation); //ovo moze da pukne
            repository.save(boat);
            String link = "<a href=\"" + frontLink + ">Prijavi i rezervišivi još neku avanturu na brodu</a>";
            String fullResponse = "Uspešno ste rezervisali akciju na brod sa imenom " + quickReservation.getResource().getTitle() + "\n " +
                    "Rezervaicija broda kоšta " + quickReservation.getPrice() + "\n" +
                    "Zakazani period je od " + quickReservation.getAppointments().get(0).getStartTime().toString() + " do " +
                    quickReservation.getAppointments().get(quickReservation.getAppointments().size() - 1).getEndTime().toString();
            String email = emailService.buildHTMLEmail(client.getName(), fullResponse, link, "Potvrda brze rezervacije");
            emailService.send(client.getEmail(), email, "Potvrda brze rezervacije");

            BoatOwner boatOwner = quickReservation.getResource().getOwner();
            boatOwner.setNumOfPoints(boatOwner.getNumOfPoints() + pointlistService.getVendorPointlist().getNumOfPoints());
            boatOwnerService.addOwner(boatOwner);

            client.setNumOfPoints(client.getNumOfPoints() + pointlistService.getClientPointlist().getNumOfPoints());
            clientService.addClient(client);
            return id;
        } catch (ObjectOptimisticLockingFailureException e) {
            return null;
        }
    }

    public List<Boat> getOwnersBoats(Long owner_id) {
        return repository.findByOwnerId(owner_id);
    }

    public List<BoatReservation> getBoatReservations(Long boat_id) {
        return boatReservationService.getReservationsByBoatId(boat_id);
    }

    public List<BoatCardDTO> getOwnerBoats(Long owner_id) {
        List<Boat> boats = repository.findByOwnerId(owner_id);
        List<BoatCardDTO> boatCards = new ArrayList<BoatCardDTO>();
        for (Boat boat : boats) {
            if (boat.getDeleted()) {
                continue;
            }
            String address = boat.getAddress().getStreet() + " " + boat.getAddress().getNumber() + ", " + boat.getAddress().getPlace() + ", " + boat.getAddress().getCountry();
            String thumbnail = "./images/housenotext.png";
            if (boat.getImages().size() > 0) {
                thumbnail = boat.getImages().get(0).getPath();
            }
            boatCards.add(new BoatCardDTO(boat.getId(), thumbnail, boat.getTitle(), boat.getDescription(), address));
        }
        return boatCards;
    }

    public ResourceOwnerDTO getOwner(Long id) {
        Boat boat = this.getBoat(id);
        BoatOwner owner = boat.getOwner();
        return new ResourceOwnerDTO(owner.getId(), owner.getName(), owner.getProfileImg());
    }

    public Boat getBoat(Long id) {
        return repository.getById(id);
    }

    @Transactional(readOnly = false)
    public Boat getByIdConcurrent(Long id) throws PessimisticLockingFailureException {
        return repository.findOneById(id);
    }

    @Cacheable(value = "boatDTO", unless="#result == null")
    public BoatDTO getBoatDTO(Long id) {
        Boat bt;
        try {
            bt= this.getBoat(id);
            if (bt.getDeleted())
                return null;
        } catch (Exception e) {
            return null;
        }
        String address = bt.getAddress().getStreet() + " " + bt.getAddress().getNumber() + ", " + bt.getAddress().getPlace() + ", " + bt.getAddress().getCountry();
        List<String> images = new ArrayList<String>();
        for (Image img : bt.getImages()) {
            images.add(img.getPath());
        }
        List<BoatQuickReservationDTO> quickReservations = getQuickReservations(bt);

        BoatDTO boatDTO = new BoatDTO(bt.getId(), bt.getTitle(), address, bt.getAddress().getNumber(), bt.getAddress().getStreet(), bt.getAddress().getPlace(), bt.getAddress().getCountry(), bt.getDescription(), bt.getType(), images, bt.getRulesAndRegulations(), bt.getEngineNumber(), bt.getEngineStrength(), bt.getTopSpeed(), bt.getLength(), bt.getNavigationEquipment(), bt.getFishingEquipment(), bt.getAdditionalServices(), bt.getPricelist().getPrice(), bt.getCancellationFee(), bt.getCapacity(), quickReservations);
        boatDTO.setOwnerId(bt.getOwner().getId());

        return boatDTO;
    }

    private List<BoatQuickReservationDTO> getQuickReservations(Boat bt) {
        List<BoatQuickReservationDTO> quickReservations = new ArrayList<BoatQuickReservationDTO>();
        for (BoatReservation reservation : bt.getReservations()) {
            if (reservation.isQuickReservation() && !reservation.isDeleted())
                quickReservations.add(createBoatReservationDTO(bt.getPricelist().getPrice(), reservation));
        }
        return quickReservations;
    }

    private BoatQuickReservationDTO createBoatReservationDTO(int boatPrice, BoatReservation reservation) {
        Appointment firstAppointment = getFirstAppointment(reservation.getAppointments());
        LocalDateTime startDate = firstAppointment.getStartTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm'h'");
        String strDate = startDate.format(formatter);
        int numberOfPeople = reservation.getNumberOfClients();
        List<Tag> additionalServices = reservation.getAdditionalServices();
        int duration = reservation.getAppointments().size();
        int price = reservation.getPrice();
        boatPrice = boatPrice * duration;
        int discount = 100 - (100 * price / boatPrice);
        return new BoatQuickReservationDTO(reservation.getId(), strDate, numberOfPeople, additionalServices, duration, price, discount);
    }

    private Appointment getFirstAppointment(List<Appointment> appointments) {
        List<Appointment> sortedAppointments = getSortedAppointments(appointments);
        return sortedAppointments.get(0);
    }

    private List<Appointment> getSortedAppointments(List<Appointment> appointments) {
        Collections.sort(appointments, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                return a1.getStartTime().compareTo(a2.getStartTime());
            }
        });
        return appointments;
    }

    public void addBoat(Boat boat) {
        repository.save(boat);
    }

    @Transactional(readOnly = false)
    public boolean deleteById(Long id) {
        Boat boat;
        try {
            boat = this.getByIdConcurrent(id);
        } catch (PessimisticLockingFailureException plfe) {
            return false;
        }
        if (getReservationsForBoat(id).size() > 0)
            return false;
        boat.setDeleted(true);
        this.addBoat(boat);
        return true;
    }

    public Long createBoat(BoatDTO boat, MultipartFile[] multipartFiles) throws IOException {
        Boat bt = getBoatFromDTO(boat);
        this.addBoat(bt);
        List<String> paths = saveImages(bt, multipartFiles);
        List<Image> images = getImages(paths);
        bt.setImages(images);
        this.addBoat(bt);
        return bt.getId();
    }


    public BoatDTO updateBoat(String id, BoatDTO boatDTO, MultipartFile[] multipartFiles) throws IOException {
        Boat originalBoat = this.getBoat(Long.parseLong(id));
        Boat newBoat = getBoatFromDTO(boatDTO);
        updateBoatFromNew(originalBoat, newBoat);
        this.addBoat(originalBoat);
        List<String> paths = saveImages(originalBoat, multipartFiles);
        List<Image> images = getImages(paths);
        originalBoat.setImages(images);
        this.addBoat(originalBoat);
        return this.getBoatDTO(originalBoat.getId());
    }

    private List<Image> getImages(List<String> paths) {
        List<Image> images = new ArrayList<Image>();
        for (String path : paths) {
            Optional<Image> optImg = imageService.getImageByPath(path);
            Image img;
            img = optImg.orElseGet(() -> new Image(path));
            imageService.save(img);
            images.add(img);
        }
        return images;
    }

    private void updateBoatFromNew(Boat originalBoat, Boat newBoat) {
        originalBoat.setTitle(newBoat.getTitle());
        originalBoat.setPricelist(newBoat.getPricelist());
        originalBoat.setDescription(newBoat.getDescription());
        originalBoat.setType(newBoat.getType());
        originalBoat.setLength(newBoat.getLength());
        originalBoat.setTopSpeed(newBoat.getTopSpeed());
        originalBoat.setEngineNumber(newBoat.getEngineNumber());
        originalBoat.setEngineStrength(newBoat.getEngineStrength());
        originalBoat.setRulesAndRegulations(newBoat.getRulesAndRegulations());
        originalBoat.setAddress(newBoat.getAddress());
        originalBoat.setNavigationEquipment(newBoat.getNavigationEquipment());
        originalBoat.setFishingEquipment(newBoat.getFishingEquipment());
        originalBoat.setAdditionalServices(newBoat.getAdditionalServices());
        originalBoat.setCancellationFee(newBoat.getCancellationFee());
        originalBoat.setCapacity(newBoat.getCapacity());
        originalBoat.setImages(newBoat.getImages());
    }

    private List<String> saveImages(Boat boat, MultipartFile[] multipartFiles) throws IOException {
        List<String> paths = new ArrayList<>();
        if (multipartFiles == null) {
            return paths;
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + boat.getId());
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + boat.getId());
        savePicturesOnPath(boat, multipartFiles, paths, path);
        savePicturesOnPath(boat, multipartFiles, paths, path_target);
        if (boat.getImages() != null && boat.getImages().size() > 0) {
            for (Image image : boat.getImages()) {
                paths.add(image.getPath());
            }
        }
        return paths.stream().distinct().collect(Collectors.toList());
    }

    private void savePicturesOnPath(Boat boat, MultipartFile[] multipartFiles, List<String> paths, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        for (MultipartFile mpf : multipartFiles) {
            String fileName = mpf.getOriginalFilename();
            try (InputStream inputStream = mpf.getInputStream()) {
                Path filePath = path.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                paths.add(IMAGES_PATH + boat.getId() + "/" + fileName);
            } catch (DirectoryNotEmptyException dnee) {
                continue;
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }
    }

    private Boat getBoatFromDTO(BoatDTO boatDTO) {
        Pricelist pl = new Pricelist(boatDTO.getPrice(), new Date());
        pricelistService.addPriceList(pl);
        Address adr = new Address(boatDTO.getCity(), boatDTO.getNumber(), boatDTO.getStreet(), boatDTO.getCountry());
        addressService.addAddress(adr);
        Boat boat = new Boat(boatDTO.getName(), adr, boatDTO.getDescription(), boatDTO.getRulesAndRegulations(), pl, boatDTO.getCancellationFee(), null, boatDTO.getType(), boatDTO.getLength(), boatDTO.getEngineNumber(), boatDTO.getEngineStrength(), boatDTO.getTopSpeed(), boatDTO.getCapacity());

        List<Tag> tags = new ArrayList<Tag>();
        for (String tagText : boatDTO.getTagsText()) {
            Tag tag = new Tag(tagText);
            tagService.addTag(tag);
            tags.add(tag);
        }
        boat.setNavigationEquipment(tags);

        List<Tag> tagsFishingEquip = new ArrayList<Tag>();
        for (String tagText : boatDTO.getTagsFishingEquipText()) {
            Tag tag = new Tag(tagText);
            tagService.addTag(tag);
            tagsFishingEquip.add(tag);
        }
        boat.setFishingEquipment(tagsFishingEquip);

        List<Tag> tagsAdditionalServices = new ArrayList<Tag>();
        for (String tagText : boatDTO.getTagsAdditionalServicesText()) {
            Tag tag = new Tag(tagText);
            tagService.addTag(tag);
            tagsAdditionalServices.add(tag);
        }
        boat.setAdditionalServices(tagsAdditionalServices);

        List<Image> images = new ArrayList<Image>();
        if (boatDTO.getImagePaths() != null) {
            for (String path : boatDTO.getImagePaths()) {
                Optional<Image> optImage = imageService.getImageByPath(path);
                optImage.ifPresent(images::add);
            }
        }
        boat.setImages(images);
        return boat;
    }

    public List<ReservationDTO> getReservationsForClient(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (BoatReservation br : boatReservationService.getStandardReservations()) {
            if (Objects.equals(br.getClient().getId(), id) && !br.isBusyPeriod()) {
                reservations.add(createDTOFromReservation(br));
            }
        }
        return reservations;

    }

    public List<ReservationDTO> getReservationsForBoat(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (BoatReservation br : boatReservationService.getAll()) {
            if (Objects.equals(br.getResource().getId(), id) && !br.isBusyPeriod()) {
                reservations.add(createDTOFromReservation(br));
            }
        }
        return reservations;

    }

    public boolean haveReservations(Long id) {
        return getReservationsForBoat(id).size() > 0 || haveReservedQuickReservations(id);
    }

    private boolean haveReservedQuickReservations(Long id) {
        for (BoatReservation br : boatReservationService.getAll()) {
            if (Objects.equals(br.getResource().getId(), id) && br.isQuickReservation() && br.getClient() != null) {
                return true;
            }
        }
        return false;
    }

    public List<ReservationDTO> getReservationsForOwner(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (BoatReservation br : boatReservationService.getAll()) {
            if (Objects.equals(br.getResource().getOwner().getId(), id) && !br.isQuickReservation() && !br.isBusyPeriod()) {
                reservations.add(createDTOFromReservation(br));
            }
        }
        return reservations;

    }

    @Transactional(readOnly = false)
    public Long createReservation(NewReservationDTO dto) throws ReservationNotAvailableException {
        BoatReservation reservation;
        try {
            reservation = createFromDTO(dto);
        } catch (PessimisticLockingFailureException plfe) {
            return Long.valueOf("-1");
        }

        List<BoatReservation> reservations = boatReservationService.getPossibleCollisionReservations(reservation.getResource().getId());
        for (BoatReservation r : reservations) {
            for (Appointment a : r.getAppointments()) {
                for (Appointment newAppointment : reservation.getAppointments()) {
                    reservationService.checkAppointmentCollision(a, newAppointment);
                }
            }
        }
        Client client = clientService.getById(String.valueOf(dto.getClientId()));
        String link = "<a href=\"" + frontLink + ">Prijavi i rezervišivi još neku avanturu</a>";
        String fullResponse = "Uspešno ste rezervisali avanturu na brodu sa imenom " + reservation.getResource().getTitle() + "\n " +
                "Rezervacija broda kоšta " + reservation.getPrice() + "\n" +
                "Zakazani period je od " + reservation.getAppointments().get(0).getStartTime().toString() + " do " +
                reservation.getAppointments().get(reservation.getAppointments().size() - 1).getEndTime().toString();
        String email = emailService.buildHTMLEmail(client.getName(), fullResponse, link, "Potvrda rezervacije");
        emailService.send(client.getEmail(), email, "Potvrda rezervacije");
        boatReservationService.save(reservation);
        client.setNumOfPoints(client.getNumOfPoints() + pointlistService.getClientPointlist().getNumOfPoints());
        clientService.addClient(client);
        reservation.setClient(client);

        BoatOwner boatOwner = reservation.getResource().getOwner();
        boatOwner.setNumOfPoints(boatOwner.getNumOfPoints() + pointlistService.getVendorPointlist().getNumOfPoints());
        boatOwnerService.addOwner(boatOwner);

        boatReservationService.save(reservation);

        return reservation.getId();
    }


    @Transactional(readOnly = false)
    public BoatReservation createFromDTO(NewReservationDTO dto) throws PessimisticLockingFailureException {
        Client client = clientService.getById(dto.getClientId().toString());
        Long id = dto.getResourceId();
        Boat boat = this.getByIdConcurrent(id); //throws

        List<Appointment> appointments = new ArrayList<Appointment>();

        LocalDateTime startTime = LocalDateTime.of(dto.getStartYear(), Month.of(dto.getStartMonth()), dto.getStartDay(), dto.getStartHour(), dto.getStartMinute());
        LocalDateTime endTime = startTime.plusHours(1);

        while (startTime.isBefore(LocalDateTime.of(dto.getEndYear(), Month.of(dto.getEndMonth()), dto.getEndDay(), dto.getEndHour(), dto.getEndMinute()))) {
            appointments.add(new Appointment(startTime, endTime));
            startTime = endTime;
            endTime = startTime.plusHours(1);
        }
        appointmentService.saveAll(appointments);

        int price = boat.getPricelist().getPrice() * appointments.size();
        int discount = userCategoryService.getClientCategoryBasedOnPoints(client.getNumOfPoints()).getDiscount();

        if (discount > 0) {
            price = price * (1 - discount / 100);
        }

        List<Tag> tags = new ArrayList<Tag>();
        for (String text : dto.getAdditionalServicesStrings()) {
            Tag tag = new Tag(text);
            tags.add(tag);
        }

        tagService.saveAll(tags);

        return new BoatReservation(
                appointments,
                dto.getNumberOfClients(),
                tags,
                price,
                client,
                boat,
                dto.isBusyPeriod(), dto.isQuickReservation());
    }

    public Long createBusyPeriod(NewBusyPeriodDTO dto) {
        BoatReservation reservation = createBusyPeriodReservationFromDTO(dto);

        List<BoatReservation> reservations = boatReservationService.getPossibleCollisionReservations(reservation.getResource().getId());
        for (BoatReservation r : reservations) {
            for (Appointment a : r.getAppointments()) {
                for (Appointment newAppointment : reservation.getAppointments()) {
                    reservationService.checkAppointmentCollision(a, newAppointment);
                }
            }
        }
        boatReservationService.save(reservation);
        return reservation.getId();
    }

    private ReservationDTO createDTOFromReservation(BoatReservation r) {
        return new ReservationDTO(
                r.getAppointments(),
                r.getNumberOfClients(),
                r.getAdditionalServices(),
                r.getPrice(),
                r.getClient(),
                r.getResource().getTitle(),
                r.isBusyPeriod(),
                r.isQuickReservation(),
                r.getResource().getId(),
                r.getId(),
                "boat"
        );
    }

    private BoatReservation createBusyPeriodReservationFromDTO(NewBusyPeriodDTO dto) {

        List<Appointment> appointments = new ArrayList<Appointment>();

        LocalDateTime startTime = LocalDateTime.of(dto.getStartYear(), Month.of(dto.getStartMonth()), dto.getStartDay(), 0, 0);
        LocalDateTime endTime = startTime.plusDays(1);


        while (startTime.isBefore(LocalDateTime.of(dto.getEndYear(), Month.of(dto.getEndMonth()), dto.getEndDay(), 23, 59))) {
            appointments.add(new Appointment(startTime, endTime));
            startTime = endTime;
            endTime = startTime.plusDays(1);
        }
        appointmentService.saveAll(appointments);

        Long id = dto.getResourceId();
        Boat boat = this.getBoat(id);

        return new BoatReservation(
                appointments,
                0,
                null,
                0,
                null,
                boat,
                true,
                false

        );
    }

    public List<ReservationDTO> getBusyPeriodForBoat(Long id) {
        List<ReservationDTO> periods = new ArrayList<ReservationDTO>();

        for (BoatReservation ar : boatReservationService.getBusyPeriodForBoat(id)) {
            periods.add(createDTOFromReservation(ar));
        }

        return periods;
    }

    public boolean clientCanReview(Long resourceId, Long clientId) {

        return hasReservations(resourceId, clientId);

    }

    private boolean hasReservations(Long resourceId, Long clientId) {
        return boatReservationService.hasReservations(resourceId, clientId);
    }

    public List<ReservationDTO> getReservationsForReview(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();
        for (BoatReservation r : boatReservationService.getStandardReservations()) {
            if (!clientReviewService.reservationHasReview(r.getId())) {
                if (Objects.equals(r.getResource().getOwner().getId(), id)) {
                    int index = r.getAppointments().size() - 1;
                    LocalDateTime time = r.getAppointments().get(index).getEndTime();
                    if (time.isBefore(LocalDateTime.now())) {
                        reservations.add(createDTOFromReservation(r));
                    }

                }
            }
        }
        return reservations;
    }

    public List<String> getBoatTypes() {
        List<String> types = new ArrayList<>();
        for (Boat boat :
                getBoats()) {
            if (!types.contains(boat.getType()))
                types.add(boat.getType());
        }
        return types;
    }

    public boolean clientCanReviewVendor(Long vendorId, Long clientId) {
        return boatReservationService.clientCanReviewVendor(vendorId, clientId);
    }

    public List<String> getBoatAddress() {
        List<String> address = new ArrayList<>();
        String fullName = "";
        for (Boat boat :
                getBoats()) {
            fullName = boat.getAddress().getFullAddressName();
            if (!address.contains(fullName)) {
                address.add(fullName);
            }
        }
        return address;
    }

    public List<EntityDTO> getFilteredBoats(BoatFilterDTO boatFilterDTO) {
        if (boatFilterDTO.isBoatsChecked()) {
            ArrayList<Boat> boats = new ArrayList<>();
            for (Boat boat : getBoats()) {
                if (checkBoatType(boatFilterDTO, boat) &&
                        checkOwnerName(boatFilterDTO, boat) &&
                        checkBoatEnginePower(boatFilterDTO, boat) &&
                        checkEngineNum(boatFilterDTO, boat) &&
                        checkBoatMaxSpeed(boatFilterDTO, boat) &&
                        checkBoatCapacity(boatFilterDTO, boat) &&
                        checkReviewRating(boatFilterDTO, boat) &&
                        checkLocation(boatFilterDTO, boat) &&
                        checkCancellationFee(boatFilterDTO, boat)
                )
                    boats.add(boat);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
            String datetime = boatFilterDTO.getStartDate() + " " + boatFilterDTO.getStartTime();
            LocalDateTime startDateTime = LocalDateTime.parse(datetime, formatter);//ovde puca
            datetime = boatFilterDTO.getEndDate() + " " + boatFilterDTO.getEndTime();
            LocalDateTime endDateTime = LocalDateTime.parse(datetime, formatter);

            int numberOfDays = (int) ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()) == 0 ? 1 : (int) ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
            int numberOfHours = (int) ChronoUnit.HOURS.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());
            if (numberOfHours < 0) {
                numberOfHours += 24;
            }
            HashMap<LocalDateTime, Integer> listOfDatesBusyness = new HashMap<>();
            boolean remove = true;
            ArrayList<Boat> boatsToDelete = new ArrayList<>();
            for (Boat boat : boats) {
                if (!checkPrice(boatFilterDTO, boat.getPricelist().getPrice() * numberOfHours))
                    boats.remove(boat);
                for (int i = 0; i < numberOfDays; i++) {
                    for (int j = 0; j < numberOfHours; j++) {
                        listOfDatesBusyness.put(startDateTime.plusHours(j).plusDays(i), 0);
                    }
                }
                for (BoatReservation boatReservation : boatReservationService.getReservationsByBoatId(boat.getId())) {
                    LocalDateTime startAppointment = boatReservation.getAppointments().get(0).getStartTime();
                    LocalDateTime endAppointment = boatReservation.getAppointments().get(boatReservation.getAppointments().size() - 1).getEndTime();
                    for (LocalDateTime time : listOfDatesBusyness.keySet()) {
                        if ((startAppointment.isBefore(time) && endAppointment.isAfter(time)))
                            listOfDatesBusyness.replace(time, 1);
                    }
                }
                for (int i : listOfDatesBusyness.values()) {
                    if (i == 0) {
                        remove = false;
                        break;
                    }
                }
                if (remove)
                    boatsToDelete.add(boat);
                listOfDatesBusyness.clear();

            }
            for (Boat boat :
                    boatsToDelete) {
                boats.remove(boat);
            }
            List<EntityDTO> list = new ArrayList<>();
            for (Boat boat :
                    boats) {
                list.add(new EntityDTO(
                        boat.getTitle(),
                        "boat",
                        boat.getImages().get(0),
                        getBoatRating(boat.getId()),
                        boat.getId(),
                        boat.getAddress(),
                        boat.getPricelist().getPrice()
                ));
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    private boolean checkCancellationFee(BoatFilterDTO boatFilterDTO, Boat boat) {
        if (boatFilterDTO.isCancellationFee() && boat.getCancellationFee() == 0)
            return true;
        else if (!boatFilterDTO.isCancellationFee() && boat.getCancellationFee() != 0)
            return true;
        return false;
    }

    private boolean checkLocation(BoatFilterDTO boatFilterDTO, Boat boat) {
        if (boatFilterDTO.getLocation().isEmpty())
            return true;
        Address location = new Address(boatFilterDTO.getLocation());
        return boat.getAddress().getStreet().equals(location.getStreet()) &&
                boat.getAddress().getPlace().equals(location.getPlace()) &&
                boat.getAddress().getNumber().equals(location.getNumber()) &&
                boat.getAddress().getCountry().equals(location.getCountry());
    }

    public List<ResourceReportDTO> getOwnerResources(Long owner_id) {
        List<Boat> boats = repository.findByOwnerId(owner_id);
        List<ResourceReportDTO> resources = new ArrayList<ResourceReportDTO>();
        for (Boat resource : boats) {
            Image img = resource.getImages().get(0);
            resources.add(new ResourceReportDTO(resource.getId(), resource.getTitle(), img, clientReviewService.getRating(resource.getId(), "resource")));
        }
        return resources;
    }

    private boolean checkReviewRating(BoatFilterDTO boatFilterDTO, Boat boat) {
        List<ClientReviewDTO> list = clientReviewService.getResourceReviews(boat.getId());
        if (list.isEmpty() && (boatFilterDTO.getReviewRating().isEmpty() || boatFilterDTO.getReviewRating().equals("0")))
            return true;
        double score = list.stream().mapToDouble(ClientReviewDTO::getRating).sum() / list.size();
        return (boatFilterDTO.getReviewRating().isEmpty() || Double.parseDouble(boatFilterDTO.getReviewRating()) <= score);
    }

    private boolean checkBoatCapacity(BoatFilterDTO boatFilterDTO, Boat boat) {
        return boatFilterDTO.getBoatCapacity().isEmpty() || Integer.parseInt(boatFilterDTO.getBoatCapacity()) == boat.getCapacity();
    }

    private boolean checkBoatMaxSpeed(BoatFilterDTO boatFilterDTO, Boat boat) {
        return boatFilterDTO.getBoatMaxSpeed().isEmpty() || Double.parseDouble(boatFilterDTO.getBoatMaxSpeed()) <= boat.getTopSpeed();
    }

    private boolean checkEngineNum(BoatFilterDTO boatFilterDTO, Boat boat) {
        return boatFilterDTO.getBoatEngineNum().isEmpty() || boatFilterDTO.getBoatEngineNum().equals(boat.getEngineNumber());
    }

    private boolean checkBoatEnginePower(BoatFilterDTO boatFilterDTO, Boat boat) {
        return (boatFilterDTO.getBoatEnginePower().isEmpty() || Double.parseDouble(boatFilterDTO.getBoatEnginePower()) <= boat.getEngineStrength());
    }

    private boolean checkOwnerName(BoatFilterDTO boatFilterDTO, Boat boat) {
        return ((boat.getOwner().getName()).equals(boatFilterDTO.getBoatOwnerName()) || boatFilterDTO.getBoatOwnerName().isEmpty());
    }

    private boolean checkBoatType(BoatFilterDTO boatFilterDTO, Boat boat) {
        return boatFilterDTO.getBoatType().isEmpty() || boatFilterDTO.getBoatType().equals(boat.getType());
    }

    private boolean checkPrice(BoatFilterDTO boatFilterDTO, int price) {
        return boatFilterDTO.getPriceRange().isEmpty() || (boatFilterDTO.getPriceRange().get(0) <= price && price <= boatFilterDTO.getPriceRange().get(1));
    }

    public String subscribeBoatUserOnBoat(SubscribeDTO subscribeDTO) {
        Boat boat = getBoat(subscribeDTO.getEntityId());
        boat.getSubClientUsernames().add(subscribeDTO.getUserId());
        repository.save(boat);
        return "Uspešno ste prijavljeni na akcije ovog broda";
    }

    public Boolean isUserSubscribedToBoat(SubscribeDTO subscribeDTO) {
        Boat boat = getBoat(subscribeDTO.getEntityId());
        return boat.getSubClientUsernames().contains(subscribeDTO.getUserId());
    }

    public double getBoatRating(Long id) {
        List<ClientReviewDTO> list = clientReviewService.getResourceReviews(id);
        return list.isEmpty() ? 0 : list.stream().mapToDouble(ClientReviewDTO::getRating).sum() / list.size();
    }

    public String unsubscribeBoatUserOnBoat(SubscribeDTO subscribeDTO) {
        Boat boat = getBoat(subscribeDTO.getEntityId());
        boat.getSubClientUsernames().remove(subscribeDTO.getUserId());
        repository.save(boat);
        return "Uspešno ste se odjavili na akcije ovog broda";
    }

    public List<EntityDTO> getClientsSubscribedBoats() {
        List<EntityDTO> entities = new ArrayList<>();
        for (Boat boat : getBoats()) {
            entities.add(new EntityDTO(
                    boat.getTitle(),
                    "boat",
                    boat.getImages().get(0),
                    getBoatRating(boat.getId()),
                    boat.getId(),
                    boat.getAddress(),
                    boat.getPricelist().getPrice()
            ));
        }
        return entities;
    }

    public List<EntityDTO> findBoatsThatClientIsSubbedTo(Long client_id) {
        List<EntityDTO> boats = new ArrayList<>();

        for (Boat b : repository.findAll()) {
            if (b.getSubClientUsernames().contains(client_id)) {
                boats.add(new EntityDTO(
                        b.getTitle(),
                        "boat",
                        b.getImages().get(0),
                        getBoatRating(b.getId()),
                        b.getId(),
                        b.getAddress(),
                        b.getPricelist().getPrice()));
            }
        }

        return boats;
    }

    public String cancelBoatReservation(Long id) {
        try {
            BoatReservation boatReservation = boatReservationService.getBoatReservation(id);
            LocalDateTime now = LocalDateTime.now();
            List<Appointment> appointments = boatReservation.getAppointments();
            int numberOfDaysBetween = (int) ChronoUnit.DAYS.between(now.toLocalDate(), appointments.get(0).getStartTime());
            if (numberOfDaysBetween < 3) {
                return "Otkazivanje rezervacije je moguće najkasnije 3 dana do početka";
            }
            boatReservationService.deleteReservation(boatReservation);
            return "Uspešno ste otkazali rezervaciju vikendicu";
        } catch (Exception exception) {
            return "Otkazivanje rezervacije nije uspelo probajte ponovo";
        }
    }

    public List<EntityDTO> getEntities() {
        List<EntityDTO> entities = new ArrayList<EntityDTO>();
        for (Boat boat : repository.findAll()) {
            if (!boat.getDeleted()) {
                entities.add(
                        new EntityDTO(
                                boat.getTitle(),
                                "boat",
                                boat.getImages().get(boat.getImages().size() - 1),
                                this.getBoatRating(boat.getId()),
                                boat.getId(),
                                boat.getAddress(),
                                boat.getPricelist().getPrice()
                        )
                );
            }
        }
        return entities;
    }

    public IncomeReport getAttendanceReport(Long id, AttendanceReportParams attendanceReportParams) {
        List<LocalDateTime> allDates = getDates(attendanceReportParams.startDate, attendanceReportParams.endDate);
        List<Boat> boats = this.getOwnersBoats(id);
        List<BoatReservation> reservations = new ArrayList<BoatReservation>();

        for (Boat boat : boats) {
            reservations.addAll(this.getBoatReservations(boat.getId()));
        }

        if (attendanceReportParams.level.equals("weekly"))
            return getWeeklyAttendanceReport(reservations, allDates);

        else if (attendanceReportParams.level.equals("monthly"))
            return getMonthlyAttendanceReport(reservations, allDates);

        else
            return getYearlyAttendanceReport(reservations, allDates);
    }

    private IncomeReport getYearlyAttendanceReport(List<BoatReservation> reservations, List<LocalDateTime> allDates) {
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        LocalDateTime start = allDates.get(0);
        for (LocalDateTime date : allDates) {
            LocalDateTime lastDayOfYear = LocalDateTime.of(date.getYear(), 12, 31, 0, 0);
            if (date.isEqual(lastDayOfYear)) {
                writeToDictReport(datesIncomes, reservations, start, date);
                start = date.plusDays(1);
            }
        }
        LocalDateTime lastDayOfYear = LocalDateTime.of(allDates.get(allDates.size() - 1).getYear(), 12, 31, 0, 0);
        if (!allDates.get(allDates.size() - 1).isEqual(lastDayOfYear)) {
            writeToDictReport(datesIncomes, reservations, start, allDates.get(allDates.size() - 1));
        }
        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(true);
    }

    private IncomeReport getMonthlyAttendanceReport(List<BoatReservation> reservations, List<LocalDateTime> allDates) {
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        LocalDateTime start = allDates.get(0);
        for (LocalDateTime date : allDates) {
            int lastDayOfMonthDate = date.getMonth().length(date.toLocalDate().isLeapYear());
            if (date.getDayOfMonth() == lastDayOfMonthDate) {
                writeToDictReport(datesIncomes, reservations, start, date);
                start = date.plusDays(1);
            }
        }
        int lastDayOfMonthDate = allDates.get(allDates.size() - 1).getMonth().length(allDates.get(allDates.size() - 1).toLocalDate().isLeapYear());
        if (allDates.get(allDates.size() - 1).getDayOfMonth() != lastDayOfMonthDate) {
            writeToDictReport(datesIncomes, reservations, start, allDates.get(allDates.size() - 1));
        }
        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(true);
    }

    private IncomeReport getWeeklyAttendanceReport(List<BoatReservation> reservations, List<LocalDateTime> allDates) {
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        LocalDateTime start = allDates.get(0);
        for (LocalDateTime date : allDates) {
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                writeToDictReport(datesIncomes, reservations, start, date);
                start = date.plusDays(1);
            }
        }
        if (allDates.get(allDates.size() - 1).getDayOfWeek() != DayOfWeek.SUNDAY) {
            writeToDictReport(datesIncomes, reservations, start, allDates.get(allDates.size() - 1));
        }
        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(true);
    }

    private IncomeReport makeReportFromDict(IncomeReport incomeReport, HashMap<String, Integer> datesIncomes) {
        for (Map.Entry<String, Integer> entry : datesIncomes.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            incomeReport.addIncome(value);
            incomeReport.addDate(key);
        }
        return incomeReport;
    }

    public void writeToDictReport(HashMap<String, Integer> datesIncomes, List<BoatReservation> reservations, LocalDateTime start, LocalDateTime date) {
        int val = getNumberOfReservationsInRange(reservations, start, date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String keyS = start.format(formatter);
        String keyE = date.format(formatter);
        String key = keyS + " - " + keyE;
        try {
            int value = datesIncomes.get(key) + val;
            datesIncomes.replace(key, value);
        } catch (Exception e) {
            datesIncomes.put(key, val);
        }
    }

    public int getNumberOfReservationsInRange(List<BoatReservation> reservations, LocalDateTime startDate, LocalDateTime endDate) {
        int numOfReservations = 0;
        for (BoatReservation boatReservation : reservations) {
            if (!boatReservation.isQuickReservation() && !boatReservation.isBusyPeriod()) {
                if (ReservationInRangeAttendance(boatReservation.getAppointments(), startDate, endDate)) {
                    numOfReservations++;
                }
            }
        }
        return numOfReservations;
    }

    public boolean ReservationInRangeAttendance(List<Appointment> appointments, LocalDateTime startDate, LocalDateTime endDate) {
        for (Appointment appointment : appointments) {
            LocalDateTime date = appointment.getStartTime();
            LocalDateTime date1;
            date1 = date.withMinute(0);
            date1 = date1.withHour(0);

            if (date1.isAfter(startDate) && date1.isBefore(endDate)) {
                return true;
            }
            if (startDate.isEqual(date1) || endDate.isEqual(date1)) {
                return true;
            }
        }
        return false;
    }

    public List<LocalDateTime> getDates(String startDateStr, String endDateStr) {
        List<LocalDateTime> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDateTime startDate = LocalDate.parse(startDateStr, formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr, formatter).atStartOfDay();
        while (!startDate.isAfter(endDate)) {
            dates.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return dates;
    }

    public IncomeReport getIncomeReport(Long id, IncomeReportDateRange dataRange) {
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        List<Boat> boats = this.getOwnersBoats(id);
        List<BoatReservation> reservations = new ArrayList<BoatReservation>();

        for (Boat boat : boats) {
            reservations.addAll(this.getBoatReservations(boat.getId()));
        }

        for (BoatReservation boatReservation : reservations) {
            if (boatReservation.isQuickReservation() || boatReservation.isBusyPeriod())
                continue;
            for (Appointment appointment : boatReservation.getAppointments()) {
                if (ReservationInRange(appointment, dataRange)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                    String key = appointment.getStartTime().format(formatter);
                    try {
                        int value = datesIncomes.get(key) + boatReservation.getPrice();
                        datesIncomes.replace(key, value);
                    } catch (Exception e) {
                        datesIncomes.put(key, boatReservation.getPrice());
                    }
                }
            }
        }

        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(false);
    }

    private boolean ReservationInRange(Appointment appointment, IncomeReportDateRange dataRange) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDateTime startDate = LocalDate.parse(dataRange.startDate, formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(dataRange.endDate, formatter).atStartOfDay();
        LocalDateTime date = appointment.getStartTime();
        LocalDateTime date1;
        date1 = date.withMinute(0);
        date1 = date1.withHour(0);
        return date.isAfter(startDate) && date.isBefore(endDate) || (startDate.isEqual(date1) || endDate.isEqual(date1));
    }
}