package com.project.team9.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.team9.model.Address;
import com.project.team9.model.Tag;
import com.project.team9.model.buissness.Pricelist;
import com.project.team9.model.reservation.AdventureReservation;
import com.project.team9.model.reservation.BoatReservation;
import com.project.team9.model.user.vendor.FishingInstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Adventure extends Resource {
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    private FishingInstructor owner;
    private int numberOfClients;
    @OneToMany
    private List<Tag> fishingEquipment;
    @OneToMany(cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true, mappedBy = "resource")
    private List<AdventureReservation> quickReservations;


    public Adventure() {
    }

    public Adventure(String title,
                     Address address,
                     String description,
                     String rulesAndRegulations,
                     Pricelist pricelist,
                     int cancellationFee,
                     FishingInstructor owner,
                     int numberOfClients
                     ) {
        super(title, address, description, rulesAndRegulations, pricelist, cancellationFee);
        this.owner = owner;
        this.numberOfClients = numberOfClients;
        this.fishingEquipment = new ArrayList<Tag>();
        this.quickReservations = new ArrayList<AdventureReservation>();
    }


    @JsonManagedReference
    public List<AdventureReservation> getQuickReservations() {
        return quickReservations;
    }

    public void addQuickReservations(AdventureReservation quickReservation) {
        this.quickReservations.add(quickReservation);
    }
    public FishingInstructor getOwner() {
        return owner;
    }

    public void setOwner(FishingInstructor owner) {
        this.owner = owner;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public List<Tag> getFishingEquipment() {
        return fishingEquipment;
    }

    public void setFishingEquipment(List<Tag> fishingEquipment) {
        this.fishingEquipment = fishingEquipment;
    }

    public void addFishingEquipment(Tag pieceOfFishingEquipment) {
        this.fishingEquipment.add(pieceOfFishingEquipment);
    }

    public void addQuickReservation(AdventureReservation adventureReservation) {
        quickReservations.add(adventureReservation);
    }

    public void removeQuickReservation(AdventureReservation quickReservation) {
        quickReservations.remove(quickReservation);
    }

    public void addReservation(AdventureReservation r1) {
    }
}
