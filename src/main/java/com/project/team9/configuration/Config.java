package com.project.team9.configuration;

import com.project.team9.model.Address;
import com.project.team9.model.Image;
import com.project.team9.model.Tag;
import com.project.team9.model.buissness.Pointlist;
import com.project.team9.model.buissness.Pricelist;
import com.project.team9.model.buissness.SiteFee;
import com.project.team9.model.buissness.UserCategory;
import com.project.team9.model.request.*;
import com.project.team9.model.reservation.AdventureReservation;
import com.project.team9.model.reservation.Appointment;
import com.project.team9.model.reservation.BoatReservation;
import com.project.team9.model.reservation.VacationHouseReservation;
import com.project.team9.model.resource.Adventure;
import com.project.team9.model.resource.Boat;
import com.project.team9.model.resource.Resource;
import com.project.team9.model.review.ClientReview;
import com.project.team9.model.user.Administrator;
import com.project.team9.model.user.Client;
import com.project.team9.model.resource.VacationHouse;
import com.project.team9.model.user.Role;
import com.project.team9.model.user.vendor.BoatOwner;
import com.project.team9.model.user.vendor.FishingInstructor;
import com.project.team9.model.user.vendor.VacationHouseOwner;
import com.project.team9.model.user.vendor.Vendor;
import com.project.team9.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class Config {
    AdventureRepository adventureRepository;
    FishingInstructorRepository fishingInstructorRepository;
    PricelistRepository pricelistRepository;
    AddressRepository addressRepository;
    TagRepository tagRepository;
    AdventureReservationRepository adventureReservationRepository;
    AppointmentRepository appointmentRepository;
    ImageRepository imageRepository;
    ClientRepository clientRepository;
    VacationHouseOwnerRepository vacationHouseOwnerRepository;
    VacationHouseRepository vacationHouseRepository;
    RoleRepository roleRepository;
    BoatOwnerRepository boatOwnerRepository;
    BoatRepository boatRepository;
    VacationHouseReservationRepository vacationHouseReservationRepository;
    BoatReservationRepository boatReservationRepository;
    ClientReviewRepository clientReviewRepository;
    RegistrationRequestRepository registrationRequestRepository;
    DeleteRequestRepository deleteRequestRepository;
    AdministratorRepository administratorRepository;
    TestData testData;
    VendorReviewRequestRepository vendorReviewRequestRepository;
    ClientReviewRequestRepository clientReviewRequestRepository;
    ComplaintsRepository complaintsRepository;
    UserCategoryRepository userCategoryRepository;
    SiteFeeRepository siteFeeRepository;
    PointlistRepository pointlistRepository;

    private Random random;
    private HashMap<Integer, String> messages;


    @Bean
    CommandLineRunner configureTestData(SiteFeeRepository siteFeeRepository, PointlistRepository pointlistRepository, UserCategoryRepository userCategoryRepository, ComplaintsRepository complaintsRepository,ClientReviewRequestRepository clientReviewRequestRepository, VendorReviewRequestRepository vendorReviewRequestRepository, AdventureRepository adventureRepository, FishingInstructorRepository fishingInstructorRepository, PricelistRepository pricelistRepository, AddressRepository addressRepository, TagRepository tagRepository, AdventureReservationRepository adventureReservationRepository, AppointmentRepository appointmentRepository, ImageRepository imageRepository, ClientRepository clientRepository, VacationHouseOwnerRepository vacationHouseOwnerRepository, VacationHouseRepository vacationHouseRepository, RoleRepository roleRepository, BoatOwnerRepository boatOwnerRepository, BoatRepository boatRepository, VacationHouseReservationRepository vacationHouseReservationRepository, BoatReservationRepository boatReservationRepository, ClientReviewRepository clientReviewRepository, RegistrationRequestRepository registrationRequestRepository, DeleteRequestRepository deleteRequestRepository, AdministratorRepository administratorRepository, TestData testData) {
        this.complaintsRepository=complaintsRepository;
        this.clientReviewRequestRepository = clientReviewRequestRepository;
        this.adventureRepository = adventureRepository;
        this.vendorReviewRequestRepository = vendorReviewRequestRepository;
        this.fishingInstructorRepository = fishingInstructorRepository;
        this.pricelistRepository = pricelistRepository;
        this.addressRepository = addressRepository;
        this.tagRepository = tagRepository;
        this.adventureReservationRepository = adventureReservationRepository;
        this.appointmentRepository = appointmentRepository;
        this.imageRepository = imageRepository;
        this.clientRepository = clientRepository;
        this.vacationHouseOwnerRepository = vacationHouseOwnerRepository;
        this.vacationHouseRepository = vacationHouseRepository;
        this.roleRepository = roleRepository;
        this.boatOwnerRepository = boatOwnerRepository;
        this.boatRepository = boatRepository;
        this.vacationHouseReservationRepository = vacationHouseReservationRepository;
        this.boatReservationRepository = boatReservationRepository;
        this.clientReviewRepository = clientReviewRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.deleteRequestRepository = deleteRequestRepository;
        this.administratorRepository = administratorRepository;
        this.userCategoryRepository = userCategoryRepository;
        this.siteFeeRepository = siteFeeRepository;
        this.pointlistRepository = pointlistRepository;

        this.testData = testData;
        this.random = new Random();
        this.messages = new HashMap<Integer, String>();

        messages.put(1, "Veoma loše!");
        messages.put(2, "Postoji i bolje.");
        messages.put(3, "Solidno.");
        messages.put(4, "Vrlo dobro.");
        messages.put(5, "Odlično!");


        return args -> {
            fillWithTestData();
        };
    }


    private void fillWithTestData() {


        //-----------------------------------------------------------

        Role roleAdministrator = new Role("ADMINISTRATOR");
        roleRepository.save(roleAdministrator);

        addAdministrator("16", "/images/administrators/1/admin1.jpg", 1L, "Milica", "Todorov", roleAdministrator, true);
        addAdministrator("17", "/images/administrators/2/admin2.jpg", 2L, "Jovan", "Smiljanski", roleAdministrator, false);
        //-------------------------------------------------------------------


        Role roleClient = new Role("CLIENT");
        roleRepository.save(roleClient);

        Client client3 = getClient("/images/clients/3/client_3.jpg", "3", 3L, "Verica", "Markov", roleClient);
        Client client4 = getClient("/images/clients/4/client_4.jpg", "4", 4L, "Jovanka", "Prodanov", roleClient);
        Client client5 = getClient("/images/clients/5/client_5.jpg", "5", 5L, "Lena", "Sudarski", roleClient);
        Client client6 = getClient("/images/clients/6/client_6.jpg", "6", 6L, "Adrijana", "Radanov", roleClient);

        //--------------------------------------------------------------

        Role roleVacationHouseOwner = new Role("VACATION_HOUSE_OWNER");
        roleRepository.save(roleVacationHouseOwner);

        VacationHouseOwner vacationHouseOwner7 = getVacationHouseOwner("/images/houseOwners/7/vacation_house_owner_7.jpg", "7", 7L, "Sreten", "Petrov", roleVacationHouseOwner);
        VacationHouseOwner vacationHouseOwner8 = getVacationHouseOwner("/images/houseOwners/8/vacation_house_owner_8.jpg", "8", 8L, "Milena", "Spasojev", roleVacationHouseOwner);
        VacationHouseOwner vacationHouseOwner9 = getVacationHouseOwner("/images/houseOwners/9/vacation_house_owner_9.jpg", "9", 9L, "Jovana", "Marin", roleVacationHouseOwner);
        VacationHouseOwner vacationHouseOwner10 = getVacationHouseOwner("/images/houseOwners/10/vacation_house_owner_10.jpg", "10", 10L, "Svetlana", "Domovin", roleVacationHouseOwner);

        //----------------------

        Role roleBoatOwner = new Role("BOAT_OWNER");
        roleRepository.save(roleBoatOwner);

        BoatOwner boatOwner11 = getBoatOwner("/images/boatOwners/11/boat_owner_11.jpg", "11", 11L, "Filip", "Jerkov", roleBoatOwner);
        BoatOwner boatOwner12 = getBoatOwner("/images/boatOwners/12/boat_owner_12.jpg", "12", 12L, "Tomislav", "Antonijev", roleBoatOwner);
        BoatOwner boatOwner13 = getBoatOwner("/images/boatOwners/13/boat_owner_13.jpg", "13", 13L, "Tatjana", "Antonijev", roleBoatOwner);
        BoatOwner boatOwner14 = getBoatOwner("/images/boatOwners/14/boat_owner_14.jpg", "14", 14L, "Natalija", "Stankov", roleBoatOwner);
        //----------------------------------------------

        Role roleFishingInstructor = new Role("FISHING_INSTRUCTOR");
        roleRepository.save(roleFishingInstructor);

        FishingInstructor fishingInstructor15 = getFishingInstructor("/images/instructors/15/fishing_instructor_15.jpg", "15", 15L, "Mirko", "Grujin", "Mnogi smatraju da je pecanje monotono, ali ja sam tu da vam pokažem kako pecanje može biti energično!", roleFishingInstructor);
        FishingInstructor fishingInstructor16 = getFishingInstructor("/images/instructors/16/fishing_instructor_16.jpg", "16", 16L, "Ena", "Jovin", "Ne odajem tajne, ovo će biti misteriozno novo iskustvo samo za hrabre.", roleFishingInstructor);
        FishingInstructor fishingInstructor17 = getFishingInstructor("/images/instructors/17/fishing_instructor_17.jpg", "17", 17L, "Ana", "Jarkov", "Sa mnom je potpuno opuštena atmosfera sa puno zezanja.", roleFishingInstructor);
        FishingInstructor fishingInstructor18 = getFishingInstructor("/images/instructors/18/fishing_instructor_18.jpg", "18", 18L, "Isidora", "Stamenkov", "Veoma profesialna osoba. Mada su mi neki dali epitet: 'blesava'.", roleFishingInstructor);

        //----------------------------------------------


        Adventure adventure1 = getAdventure("1", "/images/adventures/1/1.jpg", "/images/adventures/1/2.jpg", "/images/adventures/1/3.jpg", 1L, "Vesela avantura", "Zabavna vesela avantura za sve uzraste", "Samo bez tužnih lica.", fishingInstructor17);
        Adventure adventure2 = getAdventure("2", "/images/adventures/2/1.jpg", "/images/adventures/2/2.jpg", "/images/adventures/2/3.jpg", 2L, "Opuštena avantura", "Avantura koja će učiniti da zaboravite na stres.", "Samo bez tužnih lica.", fishingInstructor17);
        Adventure adventure3 = getAdventure("3", "/images/adventures/3/1.jpg", "/images/adventures/3/2.jpg", "/images/adventures/3/3.jpg", 3L, "Mračna avantura", "Ako niste pecali noću sada ćete.", "Tišina je krucijalna za pecanje u sumrak.", fishingInstructor16);
        Adventure adventure4 = getAdventure("4", "/images/adventures/4/1.jpg", "/images/adventures/4/2.jpg", "/images/adventures/4/3.jpg", 4L, "Brodsko pecanje", "Nema boljeg pecanja nego pecanje sa broda.", "Obavezno nositi prsluke za spasavanje.", fishingInstructor15);
        Adventure adventure5 = getAdventure("5", "/images/adventures/5/1.jpg", "/images/adventures/5/2.jpg", "/images/adventures/5/3.jpg", 5L, "Klasična avantura", "Odlična avantura za početnike", "Pažljivo slušati instrukcije.", fishingInstructor18);
        Adventure adventure6 = getAdventure("6", "/images/adventures/6/1.jpg", "/images/adventures/6/2.jpg", "/images/adventures/6/3.jpg", 6L, "Hladna avantura", "I ako se uglavnom peca u leto, otkrijte čari pecanja kada je vreme hladnije.", "Pažljivo slušati instrukcije.", fishingInstructor15);


        //----------------------------------------------

        Boat boat7 = getBoat(Arrays.asList("Švedski sto", "Neograničeno piće"), "7", 195, "/images/boats/7/1.jpg", "/images/boats/7/2.jpg", "/images/boats/7/3.jpg", 7L, "Luksuzna jahta", "Uživajte u luksuznom krstarenju na predivnoj jahti.", "Zabranjeno je naginjanje preko palube.", boatOwner11, "Jahta", 50.5, "3", 23, 115.5);
        Boat boat8 = getBoat(Arrays.asList("Pojasevi za spasavanje", "Čas vožnje"), "8", 75, "/images/boats/8/1.jpg", "/images/boats/8/2.jpg", "/images/boats/8/3.jpg", 8L, "Brzi gliser", "Ako volite brzu vožnju ovo je pravo iskustvo za vas.", "Zabranjeno je naginjanje preko palube.", boatOwner12, "Gliser", 15, "6", 89, 215);
        Boat boat9 = getBoat(Arrays.asList("Švedski sto", "Neograničeno piće", "Skakanje sa broda"), "9", 70, "/images/boats/9/1.jpg", "/images/boats/9/2.jpg", "/images/boats/9/3.jpg", 9L, "Jahta za kruziranje", "Ako ste ikada želeli da skačete u vodu na pučini ova jahta je savršena.", "Zabranjeno je skakanje bez nadzora spasioca.", boatOwner13, "Jahta", 20, "3", 23, 115.5);
        Boat boat10 = getBoat(Arrays.asList("Švedski sto", "Neograničeno piće"), "10", 70, "/images/boats/10/1.jpg", "/images/boats/10/2.jpg", "/images/boats/10/3.jpg", 10L, "Opuštanje na jahti", "Odmor, relaksacija, opuštanje... Dalji opis je nepotreban.", "Zabranjeno je skakanje u vodu.", boatOwner14, "Jahta", 20, "3", 23, 115.5);
        Boat boat11 = getBoat(Arrays.asList("Vožnja broda", "Instruktor vožnje"), "11", 70, "/images/boats/11/1.jpg", "/images/boats/11/2.jpg", "/images/boats/11/3.jpg", 11L, "Brod za nove kapetane", "Okušajte se u vožnji ovog malog broda.", "Zabranjeno je skakanje u vodu.", boatOwner13, "Brod", 16, "3", 23, 115.5);
        Boat boat12 = getBoat(Arrays.asList("Romantična večera", "Flaša vina"), "12", 70, "/images/boats/12/1.jpg", "/images/boats/12/2.jpg", "/images/boats/12/3.jpg", 12L, "Romantičan brodić", "Izvedite svog partnera partnerku na romantičnu vožnju", "Zabranjeno je skakanje u vodu.", boatOwner12, "Brod", 12, "3", 23, 115.5);


        //-----------------------------------------------------

        VacationHouse vacationHouse13 = getVacationHouse(Arrays.asList("WiFi", "Sauna"), "13", 70, "/images/houses/13/1.jpg", "/images/houses/13/2.jpg", "/images/houses/13/3.jpg", 13L, "Ušuškana idila", "Idealno mesto da odmorite od skijanja pored kamina uz toplu čokoladu.", 15, vacationHouseOwner7, 4);
        VacationHouse vacationHouse14 = getVacationHouse(Arrays.asList("WiFi", "Pogled"), "14", 120, "/images/houses/14/1.jpg", "/images/houses/14/2.jpg", "/images/houses/14/3.jpg", 14L, "Vodopadna oaza", "Zanimljiva lokacija i nezaborvno iskustvo sedenja dok voda oko vas teče.", 15, vacationHouseOwner8, 4);
        VacationHouse vacationHouse15 = getVacationHouse(Arrays.asList("WiFi", "Bazen", "Klima"), "15", 170, "/images/houses/15/1.jpg", "/images/houses/15/2.jpg", "/images/houses/15/3.jpg", 15L, "Definicija odmora", "Saznajte šta znači pravo uživanje.", 30, vacationHouseOwner9, 4);
        VacationHouse vacationHouse16 = getVacationHouse(Arrays.asList("WiFi", "Bazen"), "13", 164, "/images/houses/16/1.jpg", "/images/houses/16/2.jpg", "/images/houses/16/3.jpg", 16L, "Bela vila", "Nova vikendica sa elementima orijentalnog stila.", 12, vacationHouseOwner10, 4);
        VacationHouse vacationHouse17 = getVacationHouse(Arrays.asList("WiFi", "Sauna"), "17", 90, "/images/houses/17/1.jpg", "/images/houses/17/2.jpg", "/images/houses/17/3.jpg", 17L, "Spisateljev kutak", "Pravo mesto da se izolujete dok pišete svoje novo remek-delo.", 5, vacationHouseOwner7, 2);
        VacationHouse vacationHouse18 = getVacationHouse(Arrays.asList("WiFi", "Sauna"), "18", 70, "/images/houses/18/1.jpg", "/images/houses/18/2.jpg", "/images/houses/18/3.jpg", 18L, "Beg u šumu", "Ako vam je dosta gradske buke dođite u šumu i zaboravite na stres urbanog života.", 15, vacationHouseOwner9, 4);

        //----------------------------------------

        addRegistrationRequest("U ponudi ima 3 avanture", "aleksa123", "Aleksa", "Aleksić", "aleksa.aleksic@gmail.com", "19", "FISHING_INSTRUCTOR", "Živim na lepom plavom Dunavu.");
        addRegistrationRequest("U ponudi ima 2 vikendice", "julia123", "Julia", "Annie", "jula.annie@gmail.com", "20", "VACATION_HOUSE_OWNER", "Volim da uživam na lepoj vikendici uz čašu vina.");

        //-----------------------------------------------

        addDeleteRequest(6L, "CLIENT");
        addDeleteRequest(7L, "VACATION_HOUSE_OWNER");

        //-----------------------------------------------

        addVacationHouseReservation(vacationHouse13, client3, 6, 1, 5);
        addVacationHouseReservation(vacationHouse13, client4, 6, 15, 17);
        addVacationHouseReservation(vacationHouse13, client5, 7, 3, 7);
        addVacationHouseReservation(vacationHouse13, client6, 7, 12, 22);

        addVacationHouseQuickReservation(vacationHouse13, 6, 20, 22);
        addVacationHouseQuickReservation(vacationHouse13, 7, 25, 27);


        addVacationHouseReservation(vacationHouse14, client6, 6, 1, 5);
        addVacationHouseReservation(vacationHouse14, client3, 6, 15, 17);
        addVacationHouseReservation(vacationHouse14, client4, 7, 3, 7);
        addVacationHouseReservation(vacationHouse14, client5, 7, 12, 22);

        addVacationHouseQuickReservation(vacationHouse14, 6, 20, 22);
        addVacationHouseQuickReservation(vacationHouse14, 7, 25, 27);


        addVacationHouseReservation(vacationHouse15, client5, 6, 1, 5);
        addVacationHouseReservation(vacationHouse15, client6, 6, 15, 17);
        addVacationHouseReservation(vacationHouse15, client3, 7, 3, 7);
        addVacationHouseReservation(vacationHouse15, client4, 7, 12, 22);

        addVacationHouseQuickReservation(vacationHouse15, 6, 20, 22);
        addVacationHouseQuickReservation(vacationHouse15, 7, 25, 27);


        addVacationHouseReservation(vacationHouse16, client4, 6, 1, 5);
        addVacationHouseReservation(vacationHouse16, client5, 6, 15, 17);
        addVacationHouseReservation(vacationHouse16, client6, 7, 3, 7);
        addVacationHouseReservation(vacationHouse16, client3, 7, 12, 22);

        addVacationHouseQuickReservation(vacationHouse16, 6, 20, 22);
        addVacationHouseQuickReservation(vacationHouse16, 7, 25, 27);


        addVacationHouseReservation(vacationHouse17, client3, 6, 7, 9);
        addVacationHouseReservation(vacationHouse17, client4, 6, 23, 24);
        addVacationHouseReservation(vacationHouse17, client5, 7, 10, 23);
        addVacationHouseReservation(vacationHouse17, client6, 7, 28, 31);

        addVacationHouseQuickReservation(vacationHouse17, 6, 20, 22);
        addVacationHouseQuickReservation(vacationHouse17, 7, 25, 27);


        addVacationHouseReservation(vacationHouse18, client6, 6, 7, 9);
        addVacationHouseReservation(vacationHouse18, client3, 6, 23, 24);
        addVacationHouseReservation(vacationHouse18, client4, 7, 10, 23);
        addVacationHouseReservation(vacationHouse18, client5, 7, 28, 31);

        addVacationHouseQuickReservation(vacationHouse18, 6, 20, 22);
        addVacationHouseQuickReservation(vacationHouse18, 7, 25, 27);


        //------------------------------------------------

        addAdventureReservation(adventure1, client3, 6, 6, 10, 12);
        addAdventureReservation(adventure1, client4, 6, 10, 7, 9);
        addAdventureReservation(adventure1, client5, 6, 25, 9, 13);
        addAdventureReservation(adventure1, client6, 6, 25, 16, 18);

        addAdventureQuickReservation(adventure1, 6, 13, 10, 12);
        addAdventureQuickReservation(adventure1, 6, 18, 14, 17);


        addAdventureReservation(adventure2, client6, 6, 6, 10, 12);
        addAdventureReservation(adventure2, client3, 6, 10, 7, 9);
        addAdventureReservation(adventure2, client4, 6, 25, 9, 13);
        addAdventureReservation(adventure2, client5, 6, 25, 16, 18);

        addAdventureQuickReservation(adventure2, 6, 13, 10, 12);
        addAdventureQuickReservation(adventure2, 6, 18, 14, 17);


        addAdventureReservation(adventure3, client5, 6, 6, 10, 12);
        addAdventureReservation(adventure3, client6, 6, 10, 7, 9);
        addAdventureReservation(adventure3, client3, 6, 25, 9, 13);
        addAdventureReservation(adventure3, client4, 6, 25, 16, 18);

        addAdventureQuickReservation(adventure3, 6, 13, 10, 12);
        addAdventureQuickReservation(adventure3, 6, 18, 14, 17);


        addAdventureReservation(adventure4, client4, 6, 6, 10, 12);
        addAdventureReservation(adventure4, client5, 6, 10, 7, 9);
        addAdventureReservation(adventure4, client6, 6, 25, 9, 13);
        addAdventureReservation(adventure4, client3, 6, 25, 16, 18);

        addAdventureQuickReservation(adventure4, 6, 13, 10, 12);
        addAdventureQuickReservation(adventure4, 6, 18, 14, 17);


        addAdventureReservation(adventure5, client3, 7, 1, 10, 12);
        addAdventureReservation(adventure5, client4, 7, 8, 7, 9);
        addAdventureReservation(adventure5, client5, 6, 26, 9, 13);
        addAdventureReservation(adventure5, client6, 6, 29, 16, 18);

        addAdventureQuickReservation(adventure5, 6, 13, 15, 20);
        addAdventureQuickReservation(adventure5, 6, 18, 6, 9);


        addAdventureReservation(adventure6, client6, 7, 1, 10, 12);
        addAdventureReservation(adventure6, client3, 7, 8, 7, 9);
        addAdventureReservation(adventure6, client4, 6, 26, 9, 13);
        addAdventureReservation(adventure6, client5, 6, 29, 16, 18);

        addAdventureQuickReservation(adventure6, 6, 13, 15, 20);
        addAdventureQuickReservation(adventure6, 6, 18, 6, 9);

        //--------------------------------------------------------------

        addBoatReservation(boat7, client3, 6, 11, 7, 9);
        addBoatReservation(boat7, client4, 6, 12, 13, 15);
        addBoatReservation(boat7, client5, 6, 27, 17, 21);
        addBoatReservation(boat7, client6, 7, 9, 13, 16);

        addBoatQuickReservation(boat7, 6, 14, 8, 16);
        addBoatQuickReservation(boat7, 6, 19, 15, 21);


        addBoatReservation(boat8, client6, 6, 11, 7, 9);
        addBoatReservation(boat8, client3, 6, 12, 13, 15);
        addBoatReservation(boat8, client4, 6, 27, 17, 21);
        addBoatReservation(boat8, client5, 7, 9, 13, 16);

        addBoatQuickReservation(boat8, 6, 14, 8, 16);
        addBoatQuickReservation(boat8, 6, 19, 15, 21);


        addBoatReservation(boat9, client5, 6, 11, 7, 9);
        addBoatReservation(boat9, client6, 6, 12, 13, 15);
        addBoatReservation(boat9, client3, 6, 27, 17, 21);
        addBoatReservation(boat9, client4, 7, 9, 13, 16);

        addBoatQuickReservation(boat9, 6, 14, 8, 16);
        addBoatQuickReservation(boat9, 6, 19, 15, 21);


        addBoatReservation(boat10, client4, 6, 11, 7, 9);
        addBoatReservation(boat10, client5, 6, 12, 13, 15);
        addBoatReservation(boat10, client6, 6, 27, 17, 21);
        addBoatReservation(boat10, client3, 7, 9, 13, 16);

        addBoatQuickReservation(boat10, 6, 14, 8, 16);
        addBoatQuickReservation(boat10, 6, 19, 15, 21);

        addBoatReservation(boat11, client3, 6, 28, 7, 9);
        addBoatReservation(boat11, client4, 6, 30, 13, 15);
        addBoatReservation(boat11, client5, 7, 2, 17, 21);
        addBoatReservation(boat11, client6, 7, 24, 13, 16);

        addBoatQuickReservation(boat11, 6, 14, 8, 16);
        addBoatQuickReservation(boat11, 6, 19, 15, 21);


        addBoatReservation(boat12, client6, 6, 28, 7, 9);
        addBoatReservation(boat12, client3, 6, 30, 13, 15);
        addBoatReservation(boat12, client4, 7, 2, 17, 21);
        addBoatReservation(boat12, client5, 7, 24, 13, 16);

        addBoatQuickReservation(boat12, 6, 14, 8, 16);
        addBoatQuickReservation(boat12, 6, 19, 15, 21);


        //------------------------------------

        client3.setNumOfPoints(16);
        clientRepository.save(client3);
        client4.setNumOfPoints(16);
        clientRepository.save(client4);
        client5.setNumOfPoints(16);
        client5.setNumOfPenalties(2);
        clientRepository.save(client5);
        client6.setNumOfPoints(16);
        client6.setNumOfPenalties(7);
        clientRepository.save(client6);
//
        //------------------------------------

        addClientReviewForResource(adventure1, client3);
        addClientReviewForResource(adventure1, client4);

        addClientReviewForResource(adventure2, client3);
        addClientReviewForResource(adventure2, client4);

        addClientReviewForResource(adventure3, client3);
        addClientReviewForResource(adventure3, client4);

        addClientReviewForResource(adventure4, client3);
        addClientReviewForResource(adventure4, client4);

        addClientReviewForResource(adventure5, client3);
        addClientReviewForResource(adventure5, client4);

        addClientReviewForResource(adventure6, client3);
        addClientReviewForResource(adventure6, client4);

        addClientReviewForResource(boat7, client3);
        addClientReviewForResource(boat7, client4);

        addClientReviewForResource(boat8, client3);
        addClientReviewForResource(boat8, client4);

        addClientReviewForResource(boat9, client3);
        addClientReviewForResource(boat9, client4);

        addClientReviewForResource(boat10, client3);
        addClientReviewForResource(boat10, client4);

        addClientReviewForResource(boat11, client3);
        addClientReviewForResource(boat11, client4);

        addClientReviewForResource(boat12, client3);
        addClientReviewForResource(boat12, client4);

        addClientReviewForResource(vacationHouse13, client3);
        addClientReviewForResource(vacationHouse13, client4);

        addClientReviewForResource(vacationHouse14, client3);
        addClientReviewForResource(vacationHouse14, client4);

        addClientReviewForResource(vacationHouse15, client3);
        addClientReviewForResource(vacationHouse15, client4);

        addClientReviewForResource(vacationHouse16, client3);
        addClientReviewForResource(vacationHouse16, client4);

        addClientReviewForResource(vacationHouse17, client3);
        addClientReviewForResource(vacationHouse17, client4);

        addClientReviewForResource(vacationHouse18, client3);
        addClientReviewForResource(vacationHouse18, client4);

        //---------------------------------------------------

        addClientReviewForVendor(vacationHouseOwner7, client5);
        addClientReviewForVendor(vacationHouseOwner7, client6);

        addClientReviewForVendor(vacationHouseOwner8, client5);
        addClientReviewForVendor(vacationHouseOwner8, client6);

        addClientReviewForVendor(vacationHouseOwner9, client5);
        addClientReviewForVendor(vacationHouseOwner9, client6);

        addClientReviewForVendor(vacationHouseOwner10, client5);
        addClientReviewForVendor(vacationHouseOwner10, client6);

        addClientReviewForVendor(boatOwner11, client5);
        addClientReviewForVendor(boatOwner11, client6);

        addClientReviewForVendor(boatOwner12, client5);
        addClientReviewForVendor(boatOwner12, client6);

        addClientReviewForVendor(boatOwner13, client5);
        addClientReviewForVendor(boatOwner13, client6);

        addClientReviewForVendor(boatOwner14, client5);
        addClientReviewForVendor(boatOwner14, client6);

        addClientReviewForVendor(fishingInstructor15, client5);
        addClientReviewForVendor(fishingInstructor15, client6);

        addClientReviewForVendor(fishingInstructor16, client5);
        addClientReviewForVendor(fishingInstructor16, client6);

        addClientReviewForVendor(fishingInstructor17, client5);
        addClientReviewForVendor(fishingInstructor17, client6);

        addClientReviewForVendor(fishingInstructor18, client5);
        addClientReviewForVendor(fishingInstructor18, client6);
        //-------------------------------------------------------


        addVendorReviewRequestForAdventure(client4, adventure4, (long) 59);
        addVendorReviewRequestForBoat(client6, boat8, (long) 79);
        addVendorReviewRequestForVacationHouse(client6, vacationHouse13, (long) 4);
        //-------------------------------------------------------

        addClientRequestReview(client3, vacationHouse13, "Savrsena vikendica!", true);
        addClientRequestReview(client3, vacationHouse14, "Vrlo losa usluga! Vikendicu je nemoguce pronaci", false);

        addComplaint(client5.getId(), vacationHouse13.getId(), "VACATION_HOUSE", "Nisam se lepo proveo");
        addComplaint(client5.getId(), boatOwner13.getId(), "BOAT_OWNER", "Neljubazan covek");

        addCategory("Rozo-Plava", 0, 0, 10, "pink-blue", true, false);
        addCategory("Plavo-Ljubicasta", 11, 5, 20, "blue-purple", true, false);
        addCategory("Rozo-Plava", 0, 0, 2, "pink-blue", false, true);
        addCategory("Plavo-Ljubicasta", 3, 5, 8, "blue-purple", false, true);


        //--------------------------------------------

        addPointlist(3, "CLIENT");
        addPointlist(1, "CLIENT");
        addPointlist(7, "VENDOR");
        addPointlist(2, "VENDOR");

        addSiteFee(10);
        addSiteFee(3);

        boat7.addClient(client5);
        boatRepository.save(boat7);
        adventure1.addClient(client5);
        adventureRepository.save(adventure1);
        vacationHouse14.addClient(client5);
        vacationHouseRepository.save(vacationHouse14);

    }

    private void addSiteFee(int percentage) {
        SiteFee siteFee = new SiteFee(percentage);
        siteFeeRepository.save(siteFee);
    }

    private void addPointlist(int numOfPoints, String type) {
        Pointlist pointlist = new Pointlist(numOfPoints, type);
        pointlistRepository.save(pointlist);
    }

    private void addCategory(String name, int minimumPoints, int discount, int maximumPoints, String key, boolean isClient, boolean isVendor) {
        UserCategory userCategory = new UserCategory(
                name,
                minimumPoints,
                maximumPoints,
                discount,
                UserCategory.colors.get(key),
                isClient,
                isVendor
        );
        userCategoryRepository.save(userCategory);
    }

    private void addComplaint(Long clientId, Long entityId, String enityType, String text) {
        Complaint complaint = testData.createComplaint(clientId, entityId, enityType, text);
        complaintsRepository.save(complaint);
    }

    private void addClientRequestReview(Client client3, VacationHouse vacationHouse13, String comment, Boolean isResource) {
        int rating = random.nextInt(4) + 1;
        ClientReviewRequest clientReviewRequest = testData.createClientReviewRequestForResource(client3, vacationHouse13, rating, comment, isResource);
        clientReviewRequestRepository.save(clientReviewRequest);
    }

    private void addVendorReviewRequestForVacationHouse(Client client, VacationHouse vacationHouse, long l) {
        int rating = random.nextInt(4) + 1;
        VendorReviewRequest vendorReviewRequest = testData.createVendorReviewRequestForVacationHouse("Solidan klijent, nemam vecih primedbi. Kupatilo je ostalo malo u neredu.", vacationHouse, client.getId(), rating, false, false, l);
        vendorReviewRequestRepository.save(vendorReviewRequest);
    }

    private void addVendorReviewRequestForAdventure(Client client, Adventure adventure, long reservationId) {
        int rating = random.nextInt(4) + 1;
        VendorReviewRequest vendorReviewRequest = testData.createVendorReviewRequestForAdventure("Solidan klijent, nemam vecih primedbi. Kupatilo je ostalo malo u neredu.", adventure, client.getId(), rating, false, true, reservationId);
        vendorReviewRequestRepository.save(vendorReviewRequest);
    }

    private void addVendorReviewRequestForBoat(Client client, Boat boat, long reservationId) {
        int rating = random.nextInt(4) + 1;
        VendorReviewRequest vendorReviewRequest = testData.createVendorReviewRequestForBoat("Solidan klijent, nemam vecih primedbi. Kupatilo je ostalo malo u neredu.", boat, client.getId(), rating, true, true, reservationId);
        vendorReviewRequestRepository.save(vendorReviewRequest);
    }


    private void addClientReviewForResource(Resource resource, Client client) {
        int rating = random.nextInt(4) + 1;
        ClientReview r = testData.createClientReviewForResource(resource.getId(), client.getId(), rating, messages.get(rating));
        clientReviewRepository.save(r);
    }

    private void addClientReviewForVendor(Vendor vendor, Client client) {
        int rating = random.nextInt(4) + 1;
        ClientReview r = testData.createClientReviewForVendor(vendor.getId(), client.getId(), rating, messages.get(rating));
        clientReviewRepository.save(r);
    }


    private void addBoatReservation(Boat boat, Client client, int month, int day, int startHour, int endHour) {

        List<Appointment> reservationAppointments = new ArrayList<>();

        for (int hour = startHour; hour <= endHour; hour++) {
            reservationAppointments.add(Appointment.getHourAppointment(2022, month, day, hour, 0));
        }

        appointmentRepository.saveAll(reservationAppointments);

        int numberOfClients = random.nextInt(boat.getCapacity() - 2) + 2;

        int price = reservationAppointments.size() * boat.getPricelist().getPrice();

        BoatReservation r = testData.createBoatReservation(
                reservationAppointments,
                numberOfClients,
                new ArrayList<>(),
                price,
                client,
                boat,
                false
        );
        boatReservationRepository.save(r);

        boat.addReservation(r);
        boatRepository.save(boat);
    }

    private void addBoatQuickReservation(Boat boat, int month, int day, int startHour, int endHour) {

        List<Appointment> reservationAppointments = new ArrayList<>();

        for (int hour = startHour; hour <= endHour; hour++) {
            reservationAppointments.add(Appointment.getHourAppointment(2022, month, day, hour, 0));
        }

        appointmentRepository.saveAll(reservationAppointments);

        double discount = (0.4) * random.nextDouble();

        int numberOfClients = random.nextInt(boat.getCapacity() - 2) + 2;

        int price = (int) (reservationAppointments.size() * boat.getPricelist().getPrice() * (1 - discount));

        BoatReservation r = testData.createBoatReservation(
                reservationAppointments,
                numberOfClients,
                new ArrayList<>(),
                price,
                null,
                boat,
                true
        );

        boatReservationRepository.save(r);

        boat.addReservation(r);
        boatRepository.save(boat);
    }


    private void addAdventureReservation(Adventure adventure, Client client, int month, int day, int startHour, int endHour) {

        List<Appointment> reservationAppointments = new ArrayList<>();

        for (int hour = startHour; hour <= endHour; hour++) {
            reservationAppointments.add(Appointment.getHourAppointment(2022, month, day, hour, 0));
        }

        appointmentRepository.saveAll(reservationAppointments);

        int numberOfClients = random.nextInt(adventure.getNumberOfClients() - 2) + 2;

        int price = reservationAppointments.size() * adventure.getPricelist().getPrice();

        AdventureReservation r = testData.createAdventureReservation(
                reservationAppointments,
                numberOfClients,
                new ArrayList<>(),
                price,
                client,
                adventure,
                false
        );
        adventureReservationRepository.save(r);
        adventure.addQuickReservation(r);
        adventureRepository.save(adventure);
    }

    private void addAdventureQuickReservation(Adventure adventure, int month, int day, int startHour, int endHour) {

        List<Appointment> reservationAppointments = new ArrayList<>();

        for (int hour = startHour; hour <= endHour; hour++) {
            reservationAppointments.add(Appointment.getHourAppointment(2022, month, day, hour, 0));
        }

        appointmentRepository.saveAll(reservationAppointments);

        double discount = (0.4) * random.nextDouble();

        int numberOfClients = random.nextInt(adventure.getNumberOfClients() - 2) + 2;

        int price = (int) (reservationAppointments.size() * adventure.getPricelist().getPrice() * (1 - discount));

        AdventureReservation r = testData.createAdventureReservation(
                reservationAppointments,
                numberOfClients,
                new ArrayList<>(),
                price,
                null,
                adventure,
                true
        );
        adventureReservationRepository.save(r);
        adventure.addQuickReservation(r);
        adventureRepository.save(adventure);
    }

    private void addVacationHouseReservation(VacationHouse vacationHouse, Client client, int month, int startDay, int endDay) {

        List<Appointment> reservationAppointments = new ArrayList<>();

        for (int day = startDay; day <= endDay; day++) {
            reservationAppointments.add(Appointment.getDayAppointment(2022, month, day));
        }

        appointmentRepository.saveAll(reservationAppointments);

        int numberOfClients = random.nextInt(vacationHouse.getNumberOfRooms() * vacationHouse.getNumberOfBedsPerRoom() - 2) + 2;

        int price = reservationAppointments.size() * vacationHouse.getPricelist().getPrice();

        VacationHouseReservation r = testData.createVacationHouseReservation(
                reservationAppointments,
                numberOfClients,
                new ArrayList<>(),
                price,
                client,
                vacationHouse,
                false
        );
        vacationHouseReservationRepository.save(r);

        vacationHouse.addReservation(r);
        vacationHouseRepository.save(vacationHouse);
    }

    private void addVacationHouseQuickReservation(VacationHouse vacationHouse, int month, int startDay, int endDay) {

        List<Appointment> reservationAppointments = new ArrayList<>();

        for (int day = startDay; day <= endDay; day++) {
            reservationAppointments.add(Appointment.getDayAppointment(2022, month, day));
        }

        appointmentRepository.saveAll(reservationAppointments);

        double discount = (0.4) * random.nextDouble();

        int numberOfClients = random.nextInt(vacationHouse.getNumberOfRooms() * vacationHouse.getNumberOfBedsPerRoom() - 2) + 2;

        int price = (int) (reservationAppointments.size() * vacationHouse.getPricelist().getPrice() * (1 - discount));

        VacationHouseReservation r = testData.createVacationHouseReservation(
                reservationAppointments,
                numberOfClients,
                new ArrayList<>(),
                price,
                null,
                vacationHouse,
                true
        );
        vacationHouseReservationRepository.save(r);

        vacationHouse.addReservation(r);
        vacationHouseRepository.save(vacationHouse);
    }

    private void addDeleteRequest(long userId, String CLIENT) {
        DeleteRequest deleteRequest = testData.createDeleteRequest("Želim da mi se nalog obriše", "", userId, CLIENT);
        deleteRequestRepository.save(deleteRequest);
    }

    private void addRegistrationRequest(String text, String aleksa123, String Aleksu, String Aleksić, String email, String number, String FISHING_INSTRUCTOR, String biography) {
        RegistrationRequest registrationRequest = testData.createRegistrationRequest(text, "", aleksa123, Aleksu, Aleksić, email, "06398765421", "Novi Sad", number, "Laze Nančića", "Srbija", FISHING_INSTRUCTOR, biography, text);
        registrationRequestRepository.save(registrationRequest);
    }

    private VacationHouse getVacationHouse(List<String> texts, String number, int price, String x, String x1, String x2, long id, String Ušuškana_idila, String description, int cancellationFee, VacationHouseOwner vacationHouseOwner7, int numberOfRooms) {
        List<Tag> additionalServices13 = testData.createTagList(texts);
        tagRepository.saveAll(additionalServices13);

        Address ra13 = new Address("Novi Sad", number, "Laze Nančića", "Srbija");
        addressRepository.save(ra13);

        Pricelist pl13 = new Pricelist(price, new Date());
        pricelistRepository.save(pl13);

        List<Image> images13 = testData.createImageList(Arrays.asList(x, x1, x2));
        imageRepository.saveAll(images13);

        VacationHouse vacationHouse = testData.createVacationHouse(id, Ušuškana_idila, ra13, description, "Poštujte vreme prijavljivanja i odjavljivanja iz vikendice", pl13, cancellationFee, vacationHouseOwner7, numberOfRooms, 2, images13, additionalServices13);

        vacationHouseRepository.save(vacationHouse);

        return vacationHouse;
    }

    private Boat getBoat(List<String> texts, String number, int price, String x, String x1, String x2, long id, String Luksuzna_jahta, String description, String rulesAndRegulations, BoatOwner boatOwner11, String Jahta, double length, String engineNumber, int engineStrength, double topSpeed) {
        List<Tag> additionalServices7 = testData.createTagList(texts);
        tagRepository.saveAll(additionalServices7);
        List<Tag> navigationEquipment7 = testData.createTagList(Arrays.asList("GPS", "Radar"));
        tagRepository.saveAll(navigationEquipment7);
        List<Tag> fishingEquipment7 = testData.createTagList(Arrays.asList("Štap za pecanje", "Udice", "Silk"));
        tagRepository.saveAll(fishingEquipment7);

        Address ra7 = new Address("Novi Sad", number, "Bulevar Jovana Dučića", "Srbija");
        addressRepository.save(ra7);

        Pricelist pl7 = new Pricelist(price, new Date());
        pricelistRepository.save(pl7);

        List<Image> images7 = testData.createImageList(Arrays.asList(x, x1, x2));
        imageRepository.saveAll(images7);

        Boat boat = testData.createBoat(id, Luksuzna_jahta, ra7, description, rulesAndRegulations, pl7, 20, boatOwner11, images7, additionalServices7, navigationEquipment7, fishingEquipment7, Jahta, length, engineNumber, engineStrength, topSpeed, 72);

        boatRepository.save(boat);

        return boat;
    }

    private Adventure getAdventure(String number, String x, String x1, String x2, long id, String Vesela_avantura, String description, String rulesAndRegulations, FishingInstructor fishingInstructor17) {
        List<Tag> fishingEquipment1 = testData.createTagList(Arrays.asList("Štap za pecanje", "Udice", "Silk"));
        tagRepository.saveAll(fishingEquipment1);
        List<Tag> additionalServices1 = testData.createTagList(Arrays.asList("Pecanje na brodu", "Dodatna oprema"));
        tagRepository.saveAll(additionalServices1);

        Address ra1 = new Address("Novi Sad", number, "Mileve Marić", "Srbija");
        addressRepository.save(ra1);

        Pricelist pl1 = new Pricelist(20, new Date());
        pricelistRepository.save(pl1);

        List<Image> images1 = testData.createImageList(Arrays.asList(x, x1, x2));
        imageRepository.saveAll(images1);

        Adventure adventure = testData.createAdventure(id, Vesela_avantura, ra1, description, rulesAndRegulations, pl1, 0, fishingInstructor17, 12, images1, fishingEquipment1, additionalServices1);
        adventureRepository.save(adventure);
        return adventure;
    }

    private FishingInstructor getFishingInstructor(String path, String number, long id, String Mirko, String Grujin, String biography, Role roleFishingInstructor) {
        Image i15 = new Image(path);
        imageRepository.save(i15);

        Address a15 = new Address("Novi Sad", number, "Jevrejska", "Srbija");
        addressRepository.save(a15);

        FishingInstructor fishingInstructor15 = testData.createFishingInstructor(id, Mirko, Grujin, biography, a15, i15, roleFishingInstructor);
        fishingInstructorRepository.save(fishingInstructor15);

        return fishingInstructor15;
    }

    private BoatOwner getBoatOwner(String path, String number, long id, String Filip, String Jerkov, Role roleBoatOwner) {
        Image i11 = new Image(path);
        imageRepository.save(i11);

        Address a11 = new Address("Novi Sad", number, "Bulevar Evrope", "Srbija");
        addressRepository.save(a11);

        BoatOwner boatOwner11 = testData.createBoatOwner(id, Filip, Jerkov, a11, i11, roleBoatOwner);
        boatOwnerRepository.save(boatOwner11);

        return boatOwner11;
    }

    private VacationHouseOwner getVacationHouseOwner(String path, String number, long id, String Sreten, String Petrov, Role roleVacationHouseOwner) {
        Image i7 = new Image(path);
        imageRepository.save(i7);

        Address a7 = new Address("Novi Sad", number, "Narodnog fronta", "Srbija");
        addressRepository.save(a7);

        VacationHouseOwner vacationHouseOwner7 = testData.createVacationHouseOwner(id, Sreten, Petrov, a7, i7, roleVacationHouseOwner);
        vacationHouseOwnerRepository.save(vacationHouseOwner7);

        return vacationHouseOwner7;
    }

    private Client getClient(String path, String number, long id, String Verica, String Markov, Role roleClient) {
        Image i3 = new Image(path);
        imageRepository.save(i3);

        Address a3 = new Address("Novi Sad", number, "Kisačka", "Srbija");
        addressRepository.save(a3);

        Client client3 = testData.createClient(id, Verica, Markov, a3, i3, roleClient);
        clientRepository.save(client3);

        return client3;
    }

    private void addAdministrator(String number, String path, long id, String Milica, String Todorov, Role roleAdministrator, boolean isSuperAdministrator) {
        Address a1 = new Address("Novi Sad", number, "Bulevar Kralja Petra I", "Srbija");
        addressRepository.save(a1);

        Image i1 = new Image(path);
        imageRepository.save(i1);

        Administrator administrator1 = testData.createAdministrator(id, Milica, Todorov, a1, i1, roleAdministrator, isSuperAdministrator);
        administratorRepository.save(administrator1);
    }



}
