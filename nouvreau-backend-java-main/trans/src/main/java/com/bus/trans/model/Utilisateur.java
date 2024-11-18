package com.bus.trans.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uniqueUserNumber;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true, unique = true)  // Ajout du champ RFID
    private String rfid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateCreation;

    public Utilisateur() {
        this.uniqueUserNumber = generateUniqueUserNumber();
        this.dateCreation = new Date();
    }

    private String generateUniqueUserNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

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
