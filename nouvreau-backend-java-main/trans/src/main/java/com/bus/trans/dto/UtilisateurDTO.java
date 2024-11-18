package com.bus.trans.dto;

import java.util.Date;

public class UtilisateurDTO {

    private Long id;
    private String uniqueUserNumber;
    private String nom;
    private String prenom;
    private String role;
    private String rfid;  // Ajout du champ RFID
    private Date dateCreation;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueUserNumber() {
        return uniqueUserNumber;
    }

    public void setUniqueUserNumber(String uniqueUserNumber) {
        this.uniqueUserNumber = uniqueUserNumber;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
