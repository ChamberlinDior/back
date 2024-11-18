package com.bus.trans.dto;

import java.util.Date;

public class ReservationDTO {
    private Long id;
    private String numeroPlace;
    private boolean estReservee;
    private String nomPassager;
    private String prenomPassager;
    private Date dateNaissancePassager;
    private Date dateReservation;
    private Long trajetId;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroPlace() { return numeroPlace; }
    public void setNumeroPlace(String numeroPlace) { this.numeroPlace = numeroPlace; }

    public boolean isEstReservee() { return estReservee; }
    public void setEstReservee(boolean estReservee) { this.estReservee = estReservee; }

    public String getNomPassager() { return nomPassager; }
    public void setNomPassager(String nomPassager) { this.nomPassager = nomPassager; }

    public String getPrenomPassager() { return prenomPassager; }
    public void setPrenomPassager(String prenomPassager) { this.prenomPassager = prenomPassager; }

    public Date getDateNaissancePassager() { return dateNaissancePassager; }
    public void setDateNaissancePassager(Date dateNaissancePassager) { this.dateNaissancePassager = dateNaissancePassager; }

    public Date getDateReservation() { return dateReservation; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }

    public Long getTrajetId() { return trajetId; }
    public void setTrajetId(Long trajetId) { this.trajetId = trajetId; }
}
