package com.bus.trans.dto;

public class ReservationDTO {

    private Long trajetId;
    private Long seatId;
    private String carteClient;
    private String nom;
    private String prenom;
    private String dateNaissance;

    // Getters et Setters
    public Long getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Long trajetId) {
        this.trajetId = trajetId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getCarteClient() {
        return carteClient;
    }

    public void setCarteClient(String carteClient) {
        this.carteClient = carteClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
