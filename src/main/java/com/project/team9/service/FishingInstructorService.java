package com.project.team9.service;

import com.project.team9.dto.*;
import com.project.team9.exceptions.CannotDeleteException;
import com.project.team9.exceptions.UserNotFoundException;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.reservation.AdventureReservation;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.resource.Adventure;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.repo.AdventureRepository;
import com.project.team9.repo.FishingInstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FishingInstructorService {

    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";
    final String IMAGES_PATH = "/images/instructors/";

    private final FishingInstructorRepository repository;
    private final ImageService imageService;
    private final AdventureReservationService adventureReservationService;
    private final AdventureRepository adventureRepository;
    private final ClientReviewService clientReviewService;
    private final UserCategoryService userCategoryService;
    private final AddressService addressService;



    @Autowired
    public FishingInstructorService(FishingInstructorRepository repository, ImageService imageService, AdventureReservationService adventureReservationService, AdventureRepository adventureRepository, ClientReviewService clientReviewService, UserCategoryService userCategoryService, AddressService addressService) {
        this.repository = repository;
        this.imageService = imageService;
        this.adventureReservationService = adventureReservationService;
        this.adventureRepository = adventureRepository;
        this.clientReviewService = clientReviewService;
        this.userCategoryService = userCategoryService;
        this.addressService = addressService;
    }

    public List<FishingInstructor> getFishingInstructors() {
        return repository.findAll().stream().filter(fishingInstructor -> !fishingInstructor.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public FishingInstructor getById(String id) {
        return repository.getById(Long.parseLong(id));
    }

    public Boolean changeProfilePicture(String id, MultipartFile multipartFile) throws IOException {
        try{
            FishingInstructor instructor = this.getById(id);
            String path = saveImage(instructor, multipartFile);
            Image image = getImage(path);
            instructor.setProfileImg(image);
            this.addFishingInstructor(instructor);
            return true;
        }
        catch (Exception e){
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

    private String saveImage(FishingInstructor instructor, MultipartFile multipartFile) throws IOException {
        String pathStr = "";
        if (multipartFile == null) {
            return pathStr;
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + instructor.getId());
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + instructor.getId());
        savePictureOnPath(instructor, multipartFile, pathStr, path);
        pathStr = savePictureOnPath(instructor, multipartFile, pathStr, path_target);
        return pathStr;
    }

    private String savePictureOnPath(FishingInstructor instructor, MultipartFile mpf, String pathStr, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String fileName = mpf.getOriginalFilename();
        try (InputStream inputStream = mpf.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            pathStr = IMAGES_PATH + instructor.getId() + "/" + fileName;
        } catch (DirectoryNotEmptyException dnee) {
            throw new IOException("Directory Not Empty Exception: " + fileName, dnee);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        return pathStr;
    }

    public IncomeReport getAttendanceReport(Long id, AttendanceReportParams attendanceReportParams){
        List<LocalDateTime> allDates = getDates(attendanceReportParams.startDate, attendanceReportParams.endDate);

        List<Adventure> adventures = adventureRepository.findByOwnerId(id);
        List<AdventureReservation> reservations = new ArrayList<AdventureReservation>();

        for (Adventure adventure : adventures){
            reservations.addAll(adventureReservationService.getReservationsByAdventureId(adventure.getId()));
        }
        if (attendanceReportParams.level.equals("weekly"))
            return getWeeklyAttendanceReport(reservations, allDates);

        else if (attendanceReportParams.level.equals("monthly"))
            return getMonthlyAttendanceReport(reservations, allDates);

        else
            return getYearlyAttendanceReport(reservations, allDates);
    }

    private IncomeReport getYearlyAttendanceReport(List<AdventureReservation> reservations, List<LocalDateTime> allDates){
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        LocalDateTime start = allDates.get(0);
        for (LocalDateTime date : allDates){
            LocalDateTime lastDayOfYear  = LocalDateTime.of(date.getYear(), 12, 31, 0,0);
            if (date.isEqual(lastDayOfYear)){
                writeToDictReport(datesIncomes, reservations, start, date);
                start = date.plusDays(1);
            }
        }
        LocalDateTime lastDayOfYear  = LocalDateTime.of(allDates.get(allDates.size() -1).getYear(), 12, 31, 0,0);
        if (!allDates.get(allDates.size() -1).isEqual(lastDayOfYear)){
            writeToDictReport(datesIncomes, reservations, start, allDates.get(allDates.size() - 1));
        }
        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(true);
    }

    private IncomeReport getMonthlyAttendanceReport(List<AdventureReservation> reservations, List<LocalDateTime> allDates){
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        LocalDateTime start = allDates.get(0);
        for (LocalDateTime date : allDates){
            int lastDayOfMonthDate  = date.getMonth().length(date.toLocalDate().isLeapYear());
            if (date.getDayOfMonth() == lastDayOfMonthDate){
                writeToDictReport(datesIncomes, reservations, start, date);
                start = date.plusDays(1);
            }
        }
        int lastDayOfMonthDate  = allDates.get(allDates.size() -1).getMonth().length(allDates.get(allDates.size() -1).toLocalDate().isLeapYear());
        if (allDates.get(allDates.size() -1).getDayOfMonth() != lastDayOfMonthDate){
            writeToDictReport(datesIncomes, reservations, start, allDates.get(allDates.size() - 1));
        }
        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(true);
    }

    private IncomeReport getWeeklyAttendanceReport(List<AdventureReservation> reservations, List<LocalDateTime> allDates){
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        LocalDateTime start = allDates.get(0);
        for (LocalDateTime date : allDates){
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY){
                writeToDictReport(datesIncomes, reservations, start, date);
                start = date.plusDays(1);
            }
        }
        if (allDates.get(allDates.size() -1).getDayOfWeek() != DayOfWeek.SUNDAY){
            writeToDictReport(datesIncomes, reservations, start, allDates.get(allDates.size() - 1));
        }
        makeReportFromDict(incomeReport, datesIncomes);

        return incomeReport.sort(true);
    }

    private void makeReportFromDict(IncomeReport incomeReport, HashMap<String, Integer> datesIncomes){
        for ( Map.Entry<String, Integer> entry : datesIncomes.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            incomeReport.addIncome(value);
            incomeReport.addDate(key);
        }
    }

    private void writeToDictReport(HashMap<String, Integer> datesIncomes, List<AdventureReservation> reservations, LocalDateTime start, LocalDateTime date){
        int val = getNumberOfReservationsInRange(reservations, start, date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String keyS = start.format(formatter);
        String keyE = date.format(formatter);
        String key = keyS + " - " + keyE;
        try {
            int value  = datesIncomes.get(key) + val;
            datesIncomes.replace(key, value);
        } catch (Exception e){
            datesIncomes.put(key, val);
        }
    }

    public int getNumberOfReservationsInRange(List<AdventureReservation> reservations, LocalDateTime startDate, LocalDateTime endDate){
        int numOfReservations = 0;
        for (AdventureReservation adventureReservation : reservations){
            if (!adventureReservation.isQuickReservation() && !adventureReservation.isBusyPeriod()){
                if (ReservationInRangeAttendance(adventureReservation.getAppointments(), startDate, endDate)){
                    numOfReservations++;
                }
            }
        }
        return numOfReservations;
    }
    private boolean ReservationInRangeAttendance(List<Appointment> appointments, LocalDateTime startDate, LocalDateTime endDate){
        for (Appointment appointment: appointments){
            LocalDateTime date = appointment.getStartTime();
            LocalDateTime date1;
            date1 = date.withMinute(0);
            date1 = date1.withHour(0);

            if (date1.isAfter(startDate) && date1.isBefore(endDate)){
                return true;
            }
            if (startDate.isEqual(date1) || endDate.isEqual(date1)){
                return true;
            }
        }
        return false;
    }
    private List<LocalDateTime> getDates(String startDateStr, String endDateStr){
        List<LocalDateTime> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDateTime startDate = LocalDate.parse(startDateStr, formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr, formatter).atStartOfDay();
        while(!startDate.isAfter(endDate)){
            dates.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return dates;
    }
    public IncomeReport getIncomeReport(Long id, IncomeReportDateRange dataRange) {
        IncomeReport incomeReport = new IncomeReport();
        HashMap<String, Integer> datesIncomes = new HashMap<String, Integer>();
        List<Adventure> adventures = adventureRepository.findByOwnerId(id);
        List<AdventureReservation> reservations = new ArrayList<AdventureReservation>();

        for (Adventure adventure : adventures){
            reservations.addAll(adventureReservationService.getReservationsByAdventureId(adventure.getId()));
        }

        for (AdventureReservation adventureReservation : reservations){
            for (Appointment appointment : adventureReservation.getAppointments()){
                if (ReservationInRange(appointment, dataRange)){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                    String key = appointment.getStartTime().format(formatter);
                    try {
                        int value  = datesIncomes.get(key) + adventureReservation.getPrice();
                        datesIncomes.replace(key, value);
                    } catch (Exception e){
                        datesIncomes.put(key, adventureReservation.getPrice());
                    }
                }
            }
        }
        for ( Map.Entry<String, Integer> entry : datesIncomes.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            incomeReport.addIncome(value);
            incomeReport.addDate(key);
        }
        return incomeReport.sort(false);
    }
    private boolean ReservationInRange(Appointment appointment, IncomeReportDateRange dataRange){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDateTime startDate = LocalDate.parse(dataRange.startDate, formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(dataRange.endDate, formatter).atStartOfDay();
        LocalDateTime date = appointment.getStartTime();
        return date.isAfter(startDate) && date.isBefore(endDate);
    }

    public FishingInstructor editFishingInstructor(FishingInstructor newFishingInstructor, String id) throws UserNotFoundException {

        return repository.findById(Long.parseLong(id)).map(fishingInstructor -> {
            fishingInstructor.setFirstName(newFishingInstructor.getFirstName());
            fishingInstructor.setLastName(newFishingInstructor.getLastName());
            fishingInstructor.setPhoneNumber(newFishingInstructor.getPhoneNumber());

            return repository.save(fishingInstructor);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public FishingInstructor editFishingInstructorPassword(String newPassword, String id) throws UserNotFoundException {

        return repository.findById(Long.parseLong(id)).map(fishingInstructor -> {
            fishingInstructor.setPassword(newPassword);
            return repository.save(fishingInstructor);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public FishingInstructor getFishingInstructorByEmail(String username) {
        return repository.findByEmail(username);
    }

    public FishingInstructor addFishingInstructor(FishingInstructor fishingInstructor) {
        return repository.save(fishingInstructor);
    }

    public List<FishingInstructor> getUnregisteredFishingInstructors() {
        List<FishingInstructor> instructors = this.getFishingInstructors();
        List<FishingInstructor> filtered = new ArrayList<>();
        for (FishingInstructor instructor : instructors) {
            if (!instructor.isEnabled() && !instructor.getDeleted())
                filtered.add(instructor);
        }
        return filtered;
    }

    public List<String> getFINames() {
        List<String> names = new ArrayList<>();
        String fullName = "";
        for (FishingInstructor fishingInstructor :
                getFishingInstructors()) {
            fullName = fishingInstructor.getFirstName() + " " + fishingInstructor.getLastName();
            if (!names.contains(fullName))
                names.add(fullName);
        }
        return names;
    }

    public Long deleteById(Long id) throws CannotDeleteException {
        FishingInstructor fishingInstructor = repository.get(id);
        fishingInstructor.setDeleted(true);

        for (AdventureReservation r:  adventureReservationService.getAdventureReservationsForVendorId(id)) {
            if (r.getAppointments().get(r.getAppointments().size() - 1).getEndTime().isAfter(LocalDateTime.now())) {
                throw new CannotDeleteException();
            }
        }

        return repository.save(fishingInstructor).getId();
    }

    public UserStatDTO getUserStat(Long id) {
        FishingInstructor fishingInstructor = repository.get(id);
        return new UserStatDTO(
                0,
                fishingInstructor.getNumOfPoints(),
                userCategoryService.getVendorCategoryBasedOnPoints(fishingInstructor.getNumOfPoints()),
                clientReviewService.getRating(id, "vendor")
        );
    }

    public Long edit(FishingInstructorDTO dto) {
        Address address = new Address(dto.getPlace(), dto.getNumber(), dto.getStreet(), dto.getCountry());
        addressService.addAddress(address);

        FishingInstructor fishingInstructor = repository.getById(dto.getId());
        fishingInstructor.setFirstName(dto.getFirstName());
        fishingInstructor.setLastName(dto.getLastName());
        fishingInstructor.setPhoneNumber(dto.getPhoneNumber());
        fishingInstructor.setAddress(address);
        return repository.save(fishingInstructor).getId();
    }
}
