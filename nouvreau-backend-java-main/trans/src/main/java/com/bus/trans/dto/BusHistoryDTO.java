package com.bus.trans.dto;

import java.util.Date;

public class BusHistoryDTO {

    private Long id;
    private Long busId;
    private String chauffeurNom;
    private String chauffeurUniqueNumber;
    private String lastDestination;
    private Integer niveauBatterie;
    private boolean isCharging;
    private Date timestamp;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
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
