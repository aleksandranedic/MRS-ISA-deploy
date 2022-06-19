package com.project.team9.service;

import com.project.team9.dto.*;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.reservation.VacationHouseReservation;
import com.project.team9.model.resource.VacationHouse;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.repo.VacationHouseOwnerRepository;
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
public class VacationHouseOwnerService {

    private final VacationHouseOwnerRepository repository;
    private final AddressService addressService;
    private final ImageService imageService;
    private final VacationHouseService vacationHouseService;
    private final ClientReviewService clientReviewService;
    private final UserCategoryService userCategoryService;

    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";
    final String IMAGES_PATH = "/images/houseOwners/";

    @Autowired
    public VacationHouseOwnerService(VacationHouseOwnerRepository vacationHouseOwnerRepository, AddressService addressService, ImageService imageService, VacationHouseService vacationHouseService, ClientReviewService clientReviewService, UserCategoryService userCategoryService) {
        this.repository = vacationHouseOwnerRepository;
        this.addressService = addressService;
        this.imageService = imageService;
        this.vacationHouseService = vacationHouseService;
        this.clientReviewService = clientReviewService;
        this.userCategoryService = userCategoryService;
    }

    public VacationHouseOwner getOwner(Long id) {
        VacationHouseOwner vho = repository.getById(id);
        if (vho.getDeleted())
            return null;
        return vho;
    }

    public List<VacationHouseOwner> getVacationHouseOwners() {
        return repository.findAll().stream().filter(VacationHouseOwner -> !VacationHouseOwner.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public IncomeReport getAttendanceReport(Long id, AttendanceReportParams attendanceReportParams){
        List<LocalDateTime> allDates = getDates(attendanceReportParams.startDate, attendanceReportParams.endDate);
        List<VacationHouse> houses = vacationHouseService.getOwnersHouses(id);
        List<VacationHouseReservation> reservations = new ArrayList<VacationHouseReservation>();

        for (VacationHouse house : houses){
            reservations.addAll(vacationHouseService.getHouseReservations(house.getId()));
        }

        if (attendanceReportParams.level.equals("weekly"))
            return getWeeklyAttendanceReport(reservations, allDates);

        else if (attendanceReportParams.level.equals("monthly"))
            return getMonthlyAttendanceReport(reservations, allDates);

        else
            return getYearlyAttendanceReport(reservations, allDates);
    }

    private IncomeReport getYearlyAttendanceReport(List<VacationHouseReservation> reservations, List<LocalDateTime> allDates){
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

    private IncomeReport getMonthlyAttendanceReport(List<VacationHouseReservation> reservations, List<LocalDateTime> allDates){
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

    private IncomeReport getWeeklyAttendanceReport(List<VacationHouseReservation> reservations, List<LocalDateTime> allDates){
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

    private void writeToDictReport(HashMap<String, Integer> datesIncomes, List<VacationHouseReservation> reservations, LocalDateTime start, LocalDateTime date){
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

    public int getNumberOfReservationsInRange(List<VacationHouseReservation> reservations, LocalDateTime startDate, LocalDateTime endDate){
        int numOfReservations = 0;
        for (VacationHouseReservation vacationHouseReservation : reservations){
            if (!vacationHouseReservation.isQuickReservation() && !vacationHouseReservation.isBusyPeriod()){
                if (ReservationInRangeAttendance(vacationHouseReservation.getAppointments(), startDate, endDate)){
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
        List<VacationHouse> houses = vacationHouseService.getOwnersHouses(id);
        List<VacationHouseReservation> reservations = new ArrayList<VacationHouseReservation>();

        for (VacationHouse house : houses){
            reservations.addAll(vacationHouseService.getHouseReservations(house.getId()));
        }

        for (VacationHouseReservation vacationHouseReservation : reservations){
            for (Appointment appointment : vacationHouseReservation.getAppointments()){
                if (ReservationInRange(appointment, dataRange)){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                    String key = appointment.getStartTime().format(formatter);
                    try {
                        int value  = datesIncomes.get(key) + vacationHouseReservation.getPrice();
                        datesIncomes.replace(key, value);
                    } catch (Exception e){
                        datesIncomes.put(key, vacationHouseReservation.getPrice());
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

    public void updateOwner(Long id, UpdateOwnerDTO newOwner){
        VacationHouseOwner oldOwner = this.getOwner(id);
        updateOwner(oldOwner, newOwner);
    }
    private void updateOwner(VacationHouseOwner oldOwner, UpdateOwnerDTO newOwner){
        oldOwner.setFirstName(newOwner.getFirstName());
        oldOwner.setLastName(newOwner.getLastName());
        oldOwner.setPhoneNumber(newOwner.getPhoneNumber());
        Address oldAdr = oldOwner.getAddress();
        oldAdr.setStreet(newOwner.getStreet());
        oldAdr.setNumber(newOwner.getNumber());
        oldAdr.setPlace(newOwner.getPlace());
        oldAdr.setCountry(newOwner.getCountry());
        addressService.addAddress(oldAdr);
        this.addOwner(oldOwner);
    }
    public void addOwner(VacationHouseOwner owner) {
        repository.save(owner);
    }

    public Boolean checkPassword(Long id, String oldPassword){
        VacationHouseOwner owner = this.getOwner(id);
        return owner.getPassword().equals(oldPassword);
    }

    public void updatePassword(Long id, String newPassword){
        VacationHouseOwner owner = this.getOwner(id);
        owner.setPassword(newPassword);
        this.addOwner(owner);
    }

    public Boolean changeProfilePicture(Long id, MultipartFile multipartFile) throws IOException {
        try{
            VacationHouseOwner owner = this.getOwner(id);
            String path = saveImage(owner, multipartFile);
            Image image = getImage(path);
            owner.setProfileImg(image);
            this.save(owner);
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

    private String saveImage(VacationHouseOwner owner, MultipartFile multipartFile) throws IOException {
        String pathStr = "";
        if (multipartFile == null) {
            return pathStr;
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + owner.getId());
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + owner.getId());
        savePictureOnPath(owner, multipartFile, pathStr, path);
        pathStr = savePictureOnPath(owner, multipartFile, pathStr, path_target);
        return pathStr;
    }

    private String savePictureOnPath(VacationHouseOwner owner, MultipartFile mpf, String pathStr, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String fileName = mpf.getOriginalFilename();
        try (InputStream inputStream = mpf.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            pathStr = IMAGES_PATH + owner.getId() + "/" + fileName;
        } catch (DirectoryNotEmptyException dnee) {
            throw new IOException("Directory Not Empty Exception: " + fileName, dnee);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        return pathStr;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public VacationHouseOwner save(VacationHouseOwner owner) {
        return repository.save(owner);
    }

    public VacationHouseOwner getVacationHouseOwnerByEmail(String username) {
        return repository.findByEmail(username);
    }
    public List<VacationHouseOwner> getAll() {
        return repository.findAll().stream().filter(vacationHouseOwner -> !vacationHouseOwner.getDeleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<VacationHouseOwner> getUnregisteredVacationHouseOwners() {
        List<VacationHouseOwner> owners=this.getAll();
        List<VacationHouseOwner> filtered=new ArrayList<>();
        for (VacationHouseOwner vacationHouseOwner : owners) {
            if (!vacationHouseOwner.isEnabled() && !vacationHouseOwner.getDeleted())
                filtered.add(vacationHouseOwner);
        }
        return filtered;
    }

    public List<String> getVHONames() {
        List<String> names=new ArrayList<>();
        String fullName="";
        for (VacationHouseOwner vacationHouseOwner :
                getAll()) {
            fullName=vacationHouseOwner.getFirstName()+" "+vacationHouseOwner.getLastName();
            if(!names.contains(fullName))
                names.add(fullName);
        }
        return names;
    }

    public UserStatDTO getUserStat(Long id) {
        VacationHouseOwner houseOwner = repository.getById(id);
        return new UserStatDTO(
                0,
                houseOwner.getNumOfPoints(),
                userCategoryService.getVendorCategoryBasedOnPoints(houseOwner.getNumOfPoints()),
                clientReviewService.getRating(id, "vendor")
        );
    }
}