package com.project.team9.service;

import com.project.team9.dto.*;
import com.project.team9.exceptions.CannotDeleteException;
import com.project.team9.exceptions.ReservationNotAvailableException;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.Tag;
import com.project.team9.model.buissness.Pricelist;
import com.project.team9.model.reservation.AdventureReservation;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.reservation.BoatReservation;
import com.project.team9.model.resource.Adventure;
import com.project.team9.model.user.Client;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.repo.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdventureService {
    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";
    final String IMAGES_PATH = "/images/adventures/";

    private final AdventureRepository repository;
    private final FishingInstructorService fishingInstructorService;
    private final TagService tagService;
    private final AddressService addressService;
    private final PricelistService pricelistService;
    private final ImageService imageService;
    private final AppointmentService appointmentService;
    private final ClientService clientService;
    private final AdventureReservationService adventureReservationService;
    private final ReservationService reservationService;
    private final ClientReviewService clientReviewService;
    private final EmailService emailService;
    private final UserCategoryService userCategoryService;
    private final PointlistService pointlistService;

    @Value("${frontendlink}")
    private String frontLink;

    @Autowired
    public AdventureService(AdventureRepository adventureRepository, FishingInstructorService fishingInstructorService, TagService tagService, AddressService addressService, PricelistService pricelistService, ImageService imageService, AppointmentService appointmentService, ClientService clientService, AdventureReservationService adventureReservationService, ReservationService reservationService, UserCategoryService userCategoryService, PointlistService pointlistService, ClientReviewService clientReviewService, EmailService emailService) {
        this.repository = adventureRepository;
        this.fishingInstructorService = fishingInstructorService;
        this.tagService = tagService;
        this.addressService = addressService;
        this.pricelistService = pricelistService;
        this.imageService = imageService;
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.adventureReservationService = adventureReservationService;
        this.reservationService = reservationService;
        this.clientReviewService = clientReviewService;
        this.emailService = emailService;
        this.userCategoryService = userCategoryService;
        this.pointlistService = pointlistService;
    }

    public List<AdventureQuickReservationDTO> getQuickReservations(String id) {
        Adventure adv = this.getById(id);
        return getQuickReservations(adv);
    }

    private List<AdventureQuickReservationDTO> getQuickReservations(Adventure adv) {
        List<AdventureQuickReservationDTO> quickReservations = new ArrayList<AdventureQuickReservationDTO>();
        for (AdventureReservation reservation : adv.getQuickReservations()) {
            if (reservation.isQuickReservation() && !reservation.isDeleted())
                quickReservations.add(createAdventureReservationDTO(adv.getPricelist().getPrice(), reservation));
        }
        return quickReservations;
    }

    @Transactional(readOnly = false)
    public Boolean addQuickReservation(String id, AdventureQuickReservationDTO quickReservationDTO) throws ReservationNotAvailableException {
        Adventure adventure;
        try{
            adventure= this.getByIdConcurrent(id);
        }
        catch (PessimisticLockingFailureException plfe){
            return false;
        }
        AdventureReservation reservation = getReservationFromDTO(quickReservationDTO);
        reservation.setResource(adventure);

        List<AdventureReservation> reservations = adventureReservationService.getPossibleCollisionReservations(reservation.getResource().getId(), reservation.getResource().getOwner().getId());
        for (AdventureReservation r : reservations) {
            for (Appointment a : r.getAppointments()) {
                for (Appointment newAppointment : reservation.getAppointments()) {
                    reservationService.checkAppointmentCollision(a, newAppointment);
                }
            }
        }
        adventureReservationService.save(reservation);
        adventure.addQuickReservations(reservation);
        this.addAdventure(adventure);
        for (Long userId : adventure.getSubClientUsernames()) {
            Client client = clientService.getById(String.valueOf(userId));
            String fullResponse = "Napravljena je akcija na koji ste se preplatili\n " +
                    "Avantura kоšta " + reservation.getPrice() + "\n" +
                    "Zakazani period je od " + reservation.getAppointments().get(0).getStartTime().toString() + " do " +
                    reservation.getAppointments().get(reservation.getAppointments().size() - 1).getEndTime().toString();
            String additionalText = "<a href=\"" + this.frontLink + "\">Prijavite se i rezervišite je</a>";
            String emailForSubbedUser = emailService.buildHTMLEmail(client.getName(), fullResponse, additionalText, "Notifikacija o pretplacenim akcijama");
            emailService.send(client.getEmail(), emailForSubbedUser, "Notifikacija o pretplacenim akcijama");
        }
        return true;
    }

    private AdventureReservation getReservationFromDTO(AdventureQuickReservationDTO dto) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        String[] splitDate = dto.getStartDate().split(" ");
        String[] splitTime = splitDate[3].split(":");
        Appointment startDateAppointment = Appointment.getHourAppointment(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[0]), Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
        appointments.add(startDateAppointment);
        appointmentService.save(startDateAppointment);
        Appointment currApp = startDateAppointment;
        for (int i = 0; i < dto.getDuration() - 1; i++) {
            LocalDateTime startDate = currApp.getEndTime();
            LocalDateTime endDate = startDate.plusDays(1);
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
        AdventureReservation reservation = new AdventureReservation(dto.getNumberOfPeople(), dto.getPrice());
        reservation.setClient(null);
        reservation.setAdditionalServices(tags);
        reservation.setAppointments(appointments);
        reservation.setQuickReservation(true);

        return reservation;
    }

    public Boolean updateQuickReservation(String id, AdventureQuickReservationDTO quickReservationDTO) {
        Adventure adventure = this.getById(id);
        AdventureReservation originalReservation = adventureReservationService.getById(quickReservationDTO.getReservationID());
        if (!originalReservation.isQuickReservation())
            return false;
        AdventureReservation newReservation = getReservationFromDTO(quickReservationDTO);
        updateQuickReservation(originalReservation, newReservation);
        try {
            adventureReservationService.saveQuickReservationAsReservation(originalReservation);
        }
        catch (ObjectOptimisticLockingFailureException e) {
            return false;
        }
        this.addAdventure(adventure);
        return true;
    }

    public Boolean deleteQuickReservation(String id, String reservationID) {
        Adventure adventure = this.getById(id);
        AdventureReservation originalReservation = adventureReservationService.getById(Long.parseLong(reservationID));
        originalReservation.setDeleted(true);
        adventureReservationService.save(originalReservation);
        this.addAdventure(adventure);
        return true;
    }

    private void updateQuickReservation(AdventureReservation originalReservation, AdventureReservation newReservation) {
        originalReservation.setAppointments(newReservation.getAppointments());
        originalReservation.setAdditionalServices(newReservation.getAdditionalServices());
        originalReservation.setNumberOfClients(newReservation.getNumberOfClients());
        originalReservation.setPrice(newReservation.getPrice());
    }

    private AdventureQuickReservationDTO createAdventureReservationDTO(int boatPrice, AdventureReservation reservation) {
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
        return new AdventureQuickReservationDTO(reservation.getId(), strDate, numberOfPeople, additionalServices, duration, price, discount);
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

    public List<Adventure> getAdventures() {
        return repository.findAll().stream().filter(adventure -> !adventure.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addAdventure(Adventure adventure) {
        repository.save(adventure);
    }

    public Adventure getById(String id) {
        return repository.getById(Long.parseLong(id));
    }

    @Transactional(readOnly = false)
    public Adventure getByIdConcurrent(String id) throws PessimisticLockingFailureException {
        return repository.findOneById(Long.parseLong(id));
    }

    public AdventureDTO getDTOById(String id) {
        return new AdventureDTO(repository.getById(Long.parseLong(id)));
    }

    public Long deleteById(Long id) throws CannotDeleteException {

        for (AdventureReservation r : adventureReservationService.getReservationsByAdventureId(id)) {
            if (r.getAppointments().get(r.getAppointments().size() - 1).getEndTime().isAfter(LocalDateTime.now())) {
                throw new CannotDeleteException();
            }
        }

        Adventure adventure = repository.getById(id);
        adventure.setDeleted(true);
        return repository.save(adventure).getId();
    }

    public AdventureDTO updateAdventure(String id, AdventureDTO adventureDTO, MultipartFile[] multipartFiles) throws IOException {
        Adventure originalAdventure = this.getById(id);
        Adventure newAdventure = createAdventureFromDTO(adventureDTO);
        updateAdventureFromNew(originalAdventure, newAdventure);
        this.addAdventure(originalAdventure);
        List<String> paths = saveImages(originalAdventure, multipartFiles);
        List<Image> images = getImages(paths);
        originalAdventure.setImages(images);
        this.addAdventure(originalAdventure);
        return this.getDTOById(originalAdventure.getId().toString());
    }

    private List<String> saveImages(Adventure adventure, MultipartFile[] multipartFiles) throws IOException {
        List<String> paths = new ArrayList<>();
        if (multipartFiles == null) {
            return paths;
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + adventure.getId());
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + adventure.getId());
        savePicturesOnPath(adventure, multipartFiles, paths, path);
        savePicturesOnPath(adventure, multipartFiles, paths, path_target);
        if (adventure.getImages() != null && adventure.getImages().size() > 0) {
            for (Image image : adventure.getImages()) {
                paths.add(image.getPath());
            }
        }
        return paths.stream().distinct().collect(Collectors.toList());
    }

    private void savePicturesOnPath(Adventure adventure, MultipartFile[] multipartFiles, List<String> paths, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        for (MultipartFile mpf : multipartFiles) {
            String fileName = mpf.getOriginalFilename();
            try (InputStream inputStream = mpf.getInputStream()) {
                Path filePath = path.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                paths.add(IMAGES_PATH + adventure.getId() + "/" + fileName);
            } catch (DirectoryNotEmptyException dnee) {
                continue;
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }
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

    private void updateAdventureFromNew(Adventure oldAdventure, Adventure newAdventure) {
        oldAdventure.setTitle(newAdventure.getTitle());
        oldAdventure.setAddress(newAdventure.getAddress());
        oldAdventure.setDescription(newAdventure.getDescription());
        oldAdventure.setImages(newAdventure.getImages());
        oldAdventure.setAdditionalServices(newAdventure.getAdditionalServices());
        oldAdventure.setRulesAndRegulations(newAdventure.getRulesAndRegulations());
        oldAdventure.setPricelist(newAdventure.getPricelist());
        oldAdventure.setCancellationFee(newAdventure.getCancellationFee());
        oldAdventure.setOwner(newAdventure.getOwner());
        oldAdventure.setNumberOfClients(newAdventure.getNumberOfClients());
        oldAdventure.setFishingEquipment(newAdventure.getFishingEquipment());
    }

    public List<Adventure> findAdventuresWithOwner(String ownerId) {
        return repository.findAdventuresWithOwner(Long.parseLong(ownerId));
    }

    public Long createAdventure(AdventureDTO adventure, MultipartFile[] multipartFiles) throws IOException {

        Adventure newAdventure = createAdventureFromDTO(adventure);
        repository.save(newAdventure);
        addImagesToAdventure(multipartFiles, newAdventure);
        return newAdventure.getId();
    }

    private Adventure createAdventureFromDTO(AdventureDTO dto) {
        Pricelist pricelist = new Pricelist(dto.getPrice(), new Date());
        pricelistService.addPriceList(pricelist);

        Address address = new Address(dto.getPlace(), dto.getNumber(), dto.getStreet(), dto.getCountry());
        addressService.addAddress(address);

        FishingInstructor owner = fishingInstructorService.getById(dto.getOwnerId().toString());

        Adventure adventure = new Adventure(dto.getTitle(), address, dto.getDescription(), dto.getRulesAndRegulations(), pricelist, dto.getCancellationFee(), owner, dto.getNumberOfClients());

        for (String text : dto.getAdditionalServicesText()) {
            Tag tag = new Tag(text);
            tagService.addTag(tag);
            adventure.addAdditionalService(tag);
        }

        for (String text : dto.getFishingEquipmentText()) {
            Tag tag = new Tag(text);
            tagService.addTag(tag);
            adventure.addFishingEquipment(tag);
        }

        List<Image> images = getExistingImages(dto);
        adventure.setImages(images);

        return adventure;
    }

    private List<Image> getExistingImages(AdventureDTO dto) {
        List<Image> images = new ArrayList<Image>();
        if (dto.getImagePaths() != null) {
            for (String path : dto.getImagePaths()) {
                Optional<Image> optImage = imageService.getImageByPath(path);
                optImage.ifPresent(images::add);
            }
        }
        return images;
    }

    public Adventure editAdventure(String id, AdventureDTO dto, MultipartFile[] multipartFiles) throws IOException {
        Adventure adventure = createAdventureFromDTO(dto);
        adventure.setId(Long.parseLong(id));
        addImagesToAdventure(multipartFiles, adventure);
        repository.save(adventure);

        return adventure;
    }

    private void addImagesToAdventure(MultipartFile[] multipartFiles, Adventure adventure) throws IOException {
        List<String> paths = imageService.saveImages(adventure.getId(), multipartFiles, IMAGES_PATH, adventure.getImages());
        List<Image> images = imageService.getImages(paths);
        adventure.setImages(images);
        repository.save(adventure);
    }

    public List<ReservationDTO> getReservationsForAdventure(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (AdventureReservation ar : adventureReservationService.getStandardReservations()) {
            if (Objects.equals(ar.getResource().getId(), id)) {
                reservations.add(createDTOFromReservation(ar));

            }
        }


        return reservations;
    }

    private ReservationDTO createDTOFromReservation(AdventureReservation reservation) {
        return new ReservationDTO(reservation.getAppointments(), reservation.getNumberOfClients(), reservation.getAdditionalServices(), reservation.getPrice(), reservation.getClient(), reservation.getResource().getTitle(), reservation.isBusyPeriod(), reservation.isQuickReservation(), reservation.getResource().getId(), reservation.getId());
    }

    public List<ReservationDTO> getReservationsForFishingInstructor(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (Adventure a : this.findAdventuresWithOwner(id.toString())) {
            for (AdventureReservation ar : adventureReservationService.getStandardReservations()) {
                if (Objects.equals(ar.getResource().getId(), a.getId())) {
                    reservations.add(createDTOFromReservation(ar));
                }
            }
        }

        return reservations;
    }

    public List<ReservationDTO> getReservationsForClient(Long id) {

        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();

        for (AdventureReservation ar : adventureReservationService.getStandardReservations()) {
            if (Objects.equals(ar.getClient().getId(), id)) {
                reservations.add(createDTOFromReservation(ar));
            }
        }

        return reservations;
    }

    @Transactional(readOnly = false)
    public Long createReservation(NewReservationDTO dto) throws ReservationNotAvailableException {
        AdventureReservation reservation;
        try {
            reservation = createFromDTO(dto);
        }
        catch (PessimisticLockingFailureException plfe){
            return Long.valueOf("-1");
        }

        List<AdventureReservation> reservations = adventureReservationService.getPossibleCollisionReservations(reservation.getResource().getId(), reservation.getResource().getOwner().getId());
        for (AdventureReservation r : reservations) {
            for (Appointment a : r.getAppointments()) {
                for (Appointment newAppointment : reservation.getAppointments()) {
                    reservationService.checkAppointmentCollision(a, newAppointment);
                }
            }
        }
        adventureReservationService.save(reservation);
        Client client = clientService.getById(String.valueOf(dto.getClientId()));
        String link = "<a href=\"" +this.frontLink+ ">Prijavi i rezervišivi još neku avanturu</a>";
        String fullResponse = "Uspešno ste rezervisali avanturu sa imenom " + reservation.getResource().getTitle() + "\n " +
                "Avantura kоšta " + reservation.getPrice() + "\n" +
                "Zakazani period je od " + reservation.getAppointments().get(0).getStartTime().toString() + " do " +
                reservation.getAppointments().get(reservation.getAppointments().size() - 1).getEndTime().toString();
        String email = emailService.buildHTMLEmail(client.getName(), fullResponse, link, "Potvrda rezervacije");
        emailService.send(client.getEmail(), email, "Potvrda rezervacije");
        client.setNumOfPoints(client.getNumOfPoints() + pointlistService.getClientPointlist().getNumOfPoints());
        clientService.addClient(client);
        reservation.setClient(client);
        adventureReservationService.save(reservation);

        return reservation.getId();
    }

    @Transactional(readOnly = false)
    private AdventureReservation createFromDTO(NewReservationDTO dto) throws PessimisticLockingFailureException{
        Client client = clientService.getById(dto.getClientId().toString());
        String id = dto.getResourceId().toString();
        Adventure adventure = this.getByIdConcurrent(id); //throws PessimisticLockingFailureException

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
        appointmentService.saveAll(appointments);

        int price = adventure.getPricelist().getPrice() * appointments.size();
        int discount = userCategoryService.getClientCategoryBasedOnPoints(client.getNumOfPoints()).getDiscount();
        price = price * (1 - discount) / 100;

        List<Tag> tags = new ArrayList<Tag>();
        for (String text : dto.getAdditionalServicesStrings()) {
            Tag tag = new Tag(text);
            tags.add(tag);
        }

        tagService.saveAll(tags);

        return new AdventureReservation(appointments, dto.getNumberOfClients(), tags, price, client, adventure, dto.isBusyPeriod(), dto.isQuickReservation());
    }

    public List<ReservationDTO> getBusyPeriodsForAdventure(Long id) {
        List<ReservationDTO> periods = new ArrayList<ReservationDTO>();

        Adventure adventure = getById(id.toString());

        for (AdventureReservation ar : adventureReservationService.getBusyPeriodsForAdventure(id, adventure.getOwner().getId())) {
            periods.add(createDTOFromReservation(ar));
        }

        return periods;
    }

    public List<ReservationDTO> getBusyPeriodsForFishingInstructor(Long id) {
        List<ReservationDTO> periods = new ArrayList<ReservationDTO>();

        for (AdventureReservation ar : adventureReservationService.getBusyPeriodsForFishingInstructor(id)) {
            periods.add(createDTOFromReservation(ar));
        }

        return periods;
    }

    public Long createBusyPeriod(NewBusyPeriodDTO dto) throws ReservationNotAvailableException {

        AdventureReservation reservation = createBusyPeriodReservationFromDTO(dto);

        List<AdventureReservation> reservations = adventureReservationService.getPossibleCollisionReservations(reservation.getResource().getId(), reservation.getResource().getOwner().getId());
        for (AdventureReservation r : reservations) {
            for (Appointment a : r.getAppointments()) {
                for (Appointment newAppointment : reservation.getAppointments()) {
                    reservationService.checkAppointmentCollision(a, newAppointment);
                }
            }
        }

        adventureReservationService.save(reservation);
        return reservation.getId();
    }

    private AdventureReservation createBusyPeriodReservationFromDTO(NewBusyPeriodDTO dto) {
        List<Appointment> appointments = new ArrayList<Appointment>();

        LocalDateTime startTime = LocalDateTime.of(dto.getStartYear(), Month.of(dto.getStartMonth()), dto.getStartDay(), 0, 0);
        LocalDateTime endTime = startTime.plusDays(1);

        while (startTime.isBefore(LocalDateTime.of(dto.getEndYear(), Month.of(dto.getEndMonth()), dto.getEndDay(), 23, 59))) {
            appointments.add(new Appointment(startTime, endTime));
            startTime = endTime;
            endTime = startTime.plusHours(1);
        }
        appointmentService.saveAll(appointments);

        String id = dto.getResourceId().toString();
        Adventure adventure = this.getById(id);

        return new AdventureReservation(appointments, 0, null, 0, null, adventure, true, false

        );
    }

    public boolean clientCanReview(Long resourceId, Long clientId) {
        return hasReservations(resourceId, clientId);
    }

    public boolean hasReservations(Long resourceId, Long clientId) {
        return adventureReservationService.clientHasReservations(resourceId, clientId);
    }

    public List<ReservationDTO> getReservationsForReview(Long id) {
        List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();
        for (AdventureReservation r : adventureReservationService.getStandardReservations()) {
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

    public Long reserveQuickReservation(ReserveQuickReservationDTO dto) {
        AdventureReservation quickReservation = adventureReservationService.getById(dto.getReservationID());
        if (!quickReservation.isQuickReservation())
            return Long.valueOf("-1");
        Adventure adventure = quickReservation.getResource();
        adventure.removeQuickReservation(quickReservation);

        Client client = clientService.getById(dto.getClientID().toString());

        quickReservation.setClient(client);
        quickReservation.setQuickReservation(false);
        adventure.addQuickReservation(quickReservation);
        try{
            Long id = adventureReservationService.saveQuickReservationAsReservation(quickReservation);  // ovo moze da pukne
            String link = "<a href=\"" + this.frontLink +">Prijavi i rezervišivi još neku avanturu</a>";
            String fullResponse = "Uspešno ste rezervisali akciju na avanturu sa imenom " + quickReservation.getResource().getTitle() + "\n " +
                    "Avantura kоšta " + quickReservation.getPrice() + "\n" +
                    "Zakazani period je od " + quickReservation.getAppointments().get(0).getStartTime().toString() + " do " +
                    quickReservation.getAppointments().get(quickReservation.getAppointments().size() - 1).getEndTime().toString();
            String email = emailService.buildHTMLEmail(client.getName(), fullResponse, link, "Potvrda brze rezervacije");
            emailService.send(client.getEmail(), email, "Potvrda brze rezervacije");
            repository.save(adventure);
            return id;
        } catch (ObjectOptimisticLockingFailureException e){
            return null;
        }
    }

    public boolean clientCanReviewVendor(Long vendorId, Long clientId) {
        return adventureReservationService.clientHasReservationsWithVendor(vendorId, clientId);
    }

    public List<String> getAdventureAddress() {
        List<String> address = new ArrayList<>();
        String fullName = "";
        for (Adventure adventure :
                getAdventures()) {
            fullName = adventure.getAddress().getFullAddressName();
            if (!address.contains(fullName)) {
                address.add(fullName);
            }
        }
        return address;
    }

    public List<EntityDTO> getFilteredAdventures(AdventureFilterDTO filterDTO) {
        if (filterDTO.isAdventuresChecked()) {
            ArrayList<Adventure> adventures = new ArrayList<>();
            for (Adventure adventure : getAdventures()) {
                if (checkNumberOfClients(filterDTO, adventure) &&
                        checkInstructorName(filterDTO, adventure) &&
                        checkReviewRating(filterDTO, adventure) &&
                        checkLocation(filterDTO, adventure) &&
                        checkCancellationFee(filterDTO, adventure)
                )
                    adventures.add(adventure);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
            String datetime = filterDTO.getStartDate() + " " + filterDTO.getStartTime();
            LocalDateTime startDateTime = LocalDateTime.parse(datetime, formatter);//ovde puca
            datetime = filterDTO.getEndDate() + " " + filterDTO.getEndTime();
            LocalDateTime endDateTime = LocalDateTime.parse(datetime, formatter);

            int numberOfDays = (int) ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()) == 0 ? 1 : (int) ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
            int numberOfHours = (int) ChronoUnit.HOURS.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());
            if (numberOfHours < 0) {
                numberOfHours += 24;
            }
            HashMap<LocalDateTime, Integer> listOfDatesBusyness = new HashMap<>();
            boolean remove = true;
            ArrayList<Adventure> adventuresToDelete = new ArrayList<>();
            for (Adventure adventure : adventures) {
                if (!checkPrice(filterDTO, adventure.getPricelist().getPrice() * numberOfHours))  //of days ce da bude za vikendice
                    adventures.remove(adventure); //cenu za sve dane sto ostaje
                for (int i = 0; i < numberOfDays; i++) {
                    for (int j = 0; j < numberOfHours; j++) {
                        listOfDatesBusyness.put(startDateTime.plusHours(j).plusDays(i), 0);
                    }
                }
                for (AdventureReservation adventureReservation : adventureReservationService.getReservationsByAdventureId(adventure.getId())) {
                    LocalDateTime startAppointment = adventureReservation.getAppointments().get(0).getStartTime();
                    LocalDateTime endAppointment = adventureReservation.getAppointments().get(adventureReservation.getAppointments().size() - 1).getEndTime();
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
                    adventuresToDelete.add(adventure);
                listOfDatesBusyness.clear();

            }
            for (Adventure adventure :
                    adventuresToDelete) {
                adventures.remove(adventure);
            }
            List<EntityDTO> list = new ArrayList<>();
            for (Adventure adventure :
                    adventures) {
                list.add(new EntityDTO(
                        adventure.getTitle(),
                        "adventure",
                        adventure.getImages().get(0),
                        getAdventureRating(adventure.getId()),
                        adventure.getId(),
                        adventure.getAddress(),
                        adventure.getPricelist().getPrice()
                ));
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    private boolean checkNumberOfClients(AdventureFilterDTO filterDTO, Adventure adventure) {
        return filterDTO.getNumberOfClients().isEmpty() || Integer.parseInt(filterDTO.getNumberOfClients()) == adventure.getNumberOfClients();
    }

    private boolean checkInstructorName(AdventureFilterDTO filterDTO, Adventure adventure) {
        return (adventure.getOwner().getFirstName() + " " + adventure.getOwner().getLastName()).equals(filterDTO.getFishingInstructorName()) || filterDTO.getFishingInstructorName().isEmpty();
    }

    private boolean checkPrice(AdventureFilterDTO filterDTO, int price) {
        return filterDTO.getPriceRange().isEmpty() || (filterDTO.getPriceRange().get(0) <= price && price <= filterDTO.getPriceRange().get(1));
    }

    private boolean checkCancellationFee(AdventureFilterDTO filterDTO, Adventure adventure) {
        if (filterDTO.isCancellationFee() && adventure.getCancellationFee() == 0)
            return true;
        else if (!filterDTO.isCancellationFee() && adventure.getCancellationFee() != 0)
            return true;
        return false;
    }

    private boolean checkLocation(AdventureFilterDTO filterDTO, Adventure adventure) {
        if (filterDTO.getLocation().isEmpty())
            return true;
        Address location = new Address(filterDTO.getLocation());
        return adventure.getAddress().getStreet().equals(location.getStreet()) &&
                adventure.getAddress().getPlace().equals(location.getPlace()) &&
                adventure.getAddress().getNumber().equals(location.getNumber()) &&
                adventure.getAddress().getCountry().equals(location.getCountry());
    }

    public List<ResourceReportDTO> getOwnerResources(Long owner_id) {
        List<Adventure> adventures = repository.findByOwnerId(owner_id);
        List<ResourceReportDTO> resources = new ArrayList<ResourceReportDTO>();
        for (Adventure resource : adventures) {
            Image img = resource.getImages().get(0);
            resources.add(new ResourceReportDTO(resource.getId(), resource.getTitle(), img, clientReviewService.getRating(resource.getId(), "resource")));
        }
        return resources;
    }

    private boolean checkReviewRating(AdventureFilterDTO filterDTO, Adventure adventure) {
        List<ClientReviewDTO> list = clientReviewService.getResourceReviews(adventure.getId());
        if (list.isEmpty() && (filterDTO.getReviewRating().isEmpty() || filterDTO.getReviewRating().equals("0")))
            return true;
        double score = list.stream().mapToDouble(ClientReviewDTO::getRating).sum() / list.size();
        return (filterDTO.getReviewRating().isEmpty() || Double.parseDouble(filterDTO.getReviewRating()) <= score);
    }

    public double getAdventureRating(Long id) {
        List<ClientReviewDTO> list = clientReviewService.getResourceReviews(id);
        return list.isEmpty() ? 0 : list.stream().mapToDouble(ClientReviewDTO::getRating).sum() / list.size();
    }

    public String subscribeUserOnAdventure(SubscribeDTO subscribeDTO) {
        Adventure adventure = getById(String.valueOf(subscribeDTO.getEntityId()));
        adventure.getSubClientUsernames().add(subscribeDTO.getUserId());
        repository.save(adventure);
        return "Uspešno ste prijavljeni na akcije ove avanture";
    }

    public Boolean isUserSubscribedToAdventure(SubscribeDTO subscribeDTO) {
        Adventure adventure = getById(String.valueOf(subscribeDTO.getEntityId()));
        return adventure.getSubClientUsernames().contains(subscribeDTO.getUserId());
    }

    public String unsubscribeUserOnAdventure(SubscribeDTO subscribeDTO) {
        Adventure adventure = getById(String.valueOf(subscribeDTO.getEntityId()));
        adventure.getSubClientUsernames().remove(subscribeDTO.getUserId());
        repository.save(adventure);
        return "Uspešno ste se odjavili na akcije ove avanture";
    }

    public List<EntityDTO> getClientsSubscribedAdventures() {
        List<EntityDTO> entities = new ArrayList<>();
        for (Adventure adventure : getAdventures()) {
            entities.add(new EntityDTO(
                    adventure.getTitle(),
                    "adventure",
                    adventure.getImages().get(0),
                    getAdventureRating(adventure.getId()),
                    adventure.getId(),
                    adventure.getAddress(),
                    adventure.getPricelist().getPrice()
            ));
        }
        return entities;
    }

    public List<EntityDTO> findAdventuresThatClientIsSubbedTo(Long client_id) {
        List<EntityDTO> adventures = new ArrayList<>();

        for (Adventure a : repository.findAll()) {
            if (a.getSubClientUsernames().contains(client_id)) {
                adventures.add(new EntityDTO(
                        a.getTitle(),
                        "adventure",
                        a.getImages().get(0),
                        getAdventureRating(a.getId()),
                        a.getId(),
                        a.getAddress(),
                        a.getPricelist().getPrice()));
            }
        }

        return adventures;
    }

    public String cancelAdventureReservation(Long id) {
        try {
            AdventureReservation adventureReservation = adventureReservationService.getById(id);
            LocalDateTime now = LocalDateTime.now();
            int numberOfDaysBetween = (int) ChronoUnit.DAYS.between(now.toLocalDate(), adventureReservation.getAppointments().get(0).getStartTime());
            if (numberOfDaysBetween < 3) {
                return "Otkazivanje rezervacije je moguće najkasnije 3 dana do početka";
            }
            adventureReservationService.deleteReservation(adventureReservation);
            return "Uspešno ste otkazali rezervaciju avanture";
        } catch (Exception exception) {
            return "Otkazivanje rezervacije nije uspelo probajte ponovo";
        }
    }

    public List<EntityDTO> getEntities() {
        List<EntityDTO> entities = new ArrayList<EntityDTO>();
        for (Adventure adventure : repository.findAll()) {
            if (!adventure.getDeleted()) {
                entities.add(
                        new EntityDTO(
                                adventure.getTitle(),
                                "adventure",
                                adventure.getImages().get(adventure.getImages().size() - 1),
                                this.getAdventureRating(adventure.getId()),
                                adventure.getId(),
                                adventure.getAddress(),
                                adventure.getPricelist().getPrice()
                        )
                );
            }
        }
        return entities;
    }
}
