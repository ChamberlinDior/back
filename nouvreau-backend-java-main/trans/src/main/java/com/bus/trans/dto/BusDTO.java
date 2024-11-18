package com.bus.trans.dto;

import java.util.Date;

public class BusDTO {
    private Long id;
    private String modele;
    private String matricule;
    private String marque;
    private String macAddress;
    private String ipAddress;
    private String chauffeurNom;
    private String chauffeurUniqueNumber;
    private Date debutTrajet;
    private Date finTrajet;
    private String lastDestination;
    private Integer niveauBatterie;
    private boolean isCharging;
    private String anneeCreation; // Nouveau champ

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public Date getDebutTrajet() {
        return debutTrajet;
    }

    public void setDebutTrajet(Date debutTrajet) {
        this.debutTrajet = debutTrajet;
    }

    public Date getFinTrajet() {
        return finTrajet;
    }

    public void setFinTrajet(Date finTrajet) {
        this.finTrajet = finTrajet;
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

    public String getAnneeCreation() {
        return anneeCreation;
    }

    public void setAnneeCreation(String anneeCreation) {
        this.anneeCreation = anneeCreation;
    }
}
