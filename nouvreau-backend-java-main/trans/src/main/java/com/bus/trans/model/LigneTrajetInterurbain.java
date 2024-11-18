package com.bus.trans.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LigneTrajetInterurbain extends LigneTrajet {

    @Column(nullable = false)
    private String lieuDepart;

    @Column(nullable = false)
    private String lieuArrivee;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDateTime heureDepart;

    @Column(nullable = false)
    private LocalDateTime heureArrivee;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private VehiculeInterurbain vehicule;

    // Getters et Setters
    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuArrivee() {
        return lieuArrivee;
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public VehiculeInterurbain getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeInterurbain vehicule) {
        this.vehicule = vehicule;
    }
}
