package com.bus.trans.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class BusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @Column(name = "chauffeur_nom")
    private String chauffeurNom;

    @Column(name = "chauffeur_unique_number")
    private String chauffeurUniqueNumber;

    @Column(name = "last_destination")
    private String lastDestination;

    @Column(name = "niveau_batterie")
    private Integer niveauBatterie;

    @Column(name = "is_charging")
    private boolean isCharging;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getChauffeurNom() {
        return chauffeurNom;
    }

    public void setChauffeurNom(String chauffeurNom) {
        this.chauffeurNom = chauffeurNom;
    }

    public String getChauffeurUniqueNumber() {
        return chauffeurUniqueNumber;
    }

    public void setChauffeurUniqueNumber(String chauffeurUniqueNumber) {
        this.chauffeurUniqueNumber = chauffeurUniqueNumber;
    }

    public String getLastDestination() {
        return lastDestination;
    }

    public void setLastDestination(String lastDestination) {
        this.lastDestination = lastDestination;
    }

    public Integer getNiveauBatterie() {
        return niveauBatterie;
    }

    public void setNiveauBatterie(Integer niveauBatterie) {
        this.niveauBatterie = niveauBatterie;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean isCharging) {
        this.isCharging = isCharging;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
