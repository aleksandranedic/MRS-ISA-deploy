package com.project.team9.service;

import com.project.team9.dto.AdminDTO;
import com.project.team9.dto.IncomeReport;
import com.project.team9.dto.IncomeReportDateRange;
import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.user.Administrator;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.repo.AdministratorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {
    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";
    final String IMAGES_PATH = "/images/administrators/";

    private final AdministratorRepository administratorRepository;
    private final FishingInstructorService fishingInstructorService;
    private final VacationHouseOwnerService vacationHouseOwnerService;
    private final BoatOwnerService boatOwnerService;
    private final AddressService addressService;
    private final ImageService imageService;

    public AdministratorService(AdministratorRepository administratorRepository, FishingInstructorService fishingInstructorService, VacationHouseOwnerService vacationHouseOwnerService, BoatOwnerService boatOwnerService, AddressService addressService, ImageService imageService) {
        this.administratorRepository = administratorRepository;
        this.fishingInstructorService = fishingInstructorService;
        this.vacationHouseOwnerService = vacationHouseOwnerService;
        this.boatOwnerService = boatOwnerService;
        this.addressService = addressService;
        this.imageService = imageService;
    }
    public Administrator addAdmin(Administrator administrator){
       return administratorRepository.save(administrator);
    }

    public Administrator getAdministratorByEmail(String username) {
        return administratorRepository.findByEmail(username);
    }
    public IncomeReport getIncomeReportInstructors(IncomeReportDateRange dataRange){
        List<FishingInstructor> instructors = fishingInstructorService.getFishingInstructors();
        List<IncomeReport> reports = new ArrayList<>();
        for (FishingInstructor fishingInstructor : instructors ) {
            reports.add(fishingInstructorService.getIncomeReport(fishingInstructor.getId(), dataRange));
        }
        return mergeReports(reports);
    }
    public IncomeReport getIncomeReportBoatOwners(IncomeReportDateRange dataRange){
        List<BoatOwner> owners = boatOwnerService.getBoatOwners();
        List<IncomeReport> reports = new ArrayList<>();
        for (BoatOwner boatOwner : owners ) {
            reports.add(boatOwnerService.getIncomeReport(boatOwner.getId(), dataRange));
        }
        return mergeReports(reports);
    }
    public IncomeReport getIncomeReportHouseOwners(IncomeReportDateRange dataRange){
        List<VacationHouseOwner> owners = vacationHouseOwnerService.getVacationHouseOwners();
        List<IncomeReport> reports = new ArrayList<>();
        for (VacationHouseOwner vacationHouseOwner : owners ) {
            reports.add(vacationHouseOwnerService.getIncomeReport(vacationHouseOwner.getId(), dataRange));
        }
        return mergeReports(reports);
    }
    public IncomeReport getIncomeReportAll(IncomeReportDateRange dataRange){
        List<IncomeReport> reports = new ArrayList<>();
        reports.add(getIncomeReportInstructors(dataRange));
        reports.add(getIncomeReportBoatOwners(dataRange));
        reports.add(getIncomeReportHouseOwners(dataRange));
        return mergeReports(reports);
    }

    private IncomeReport mergeReports(List<IncomeReport> reports){
        IncomeReport incomeReport = new IncomeReport();
        for (IncomeReport report : reports){
            for (int i=0; i<report.getDates().size(); i++) {
                int index = incomeReport.getDates().indexOf(report.getDates().get(i));
                if (index > -1) {
                    int newVal = incomeReport.getIncomes().get(index) + report.getIncomes().get(i);
                    incomeReport.getIncomes().set(index, newVal);
                }
                else{
                    incomeReport.getDates().add(report.getDates().get(i));
                    incomeReport.getIncomes().add(report.getIncomes().get(i));
                }
            }
        }
        return incomeReport.sort(false);
    }
    public List<Administrator> getAdministrators() {
        return administratorRepository.findAll();
    }

    public Long deleteById(Long id) {
        Administrator administrator = administratorRepository.getById(id);
        administrator.setDeleted(true);
        return administratorRepository.save(administrator).getId();
    }

    public Administrator getAdmin(Long id) {
        return administratorRepository.getAdminById(id);
    }

    public void save(Administrator administrator) {
        addressService.addAddress(administrator.getAddress());
        Image image = new Image();
        imageService.save(image);
        administrator.setProfileImg(image);
        administratorRepository.save(administrator);
    }

    public Long edit(AdminDTO dto) {

        Administrator administrator = administratorRepository.getAdminById(dto.getId());

        Address address = new Address(dto.getPlace(), dto.getNumber(), dto.getStreet(), dto.getCountry());
        addressService.addAddress(address);
        administrator.setFirstName(dto.getFirstName());
        administrator.setLastName(dto.getLastName());
        administrator.setEmail(dto.getEmail());
        administrator.setPhoneNumber(dto.getPhoneNumber());
        administrator.setAddress(address);

        return administratorRepository.save(administrator).getId();

    }

    public Long editWithPhoto(AdminDTO dto, MultipartFile multipartFile) throws IOException {

        Administrator administrator = administratorRepository.getAdminById(dto.getId());

        String path = saveImage(administrator, multipartFile);
        Image image = getImage(path);
        administrator.setProfileImg(image);

        Address address = new Address(dto.getPlace(), dto.getNumber(), dto.getStreet(), dto.getCountry());
        addressService.addAddress(address);
        administrator.setFirstName(dto.getFirstName());
        administrator.setLastName(dto.getLastName());
        administrator.setEmail(dto.getEmail());
        administrator.setPhoneNumber(dto.getPhoneNumber());
        administrator.setAddress(address);

        return administratorRepository.save(administrator).getId();

    }

    private String saveImage(Administrator admin, MultipartFile multipartFile) throws IOException {
        String pathStr = "";
        if (multipartFile == null) {
            return pathStr;
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + admin.getId());
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + admin.getId());
        savePictureOnPath(admin, multipartFile, pathStr, path);
        pathStr = savePictureOnPath(admin, multipartFile, pathStr, path_target);
        return pathStr;
    }

    private String savePictureOnPath(Administrator admin, MultipartFile mpf, String pathStr, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String fileName = mpf.getOriginalFilename();
        try (InputStream inputStream = mpf.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            pathStr = IMAGES_PATH + admin.getId() + "/" + fileName;
        } catch (DirectoryNotEmptyException dnee) {
            throw new IOException("Directory Not Empty Exception: " + fileName, dnee);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        return pathStr;
    }
    private Image getImage(String path) {
        Image image;
        Optional<Image> optImg = imageService.getImageByPath(path);
        image = optImg.orElseGet(() -> new Image(path));
        imageService.save(image);
        return image;
    }


}
