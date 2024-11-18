package com.bus.trans.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numClient;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    private String quartier;
    private String ville;

    @Column(nullable = false)
    private String nomAgent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateCreation;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Carte> cartes;

    public Client() {
        this.numClient = "CLT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nomAgent = "Agent-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        this.dateCreation = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumClient() {
        return numClient;
    }

    public void setNumClient(String numClient) {
        this.numClient = numClient;
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

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Carte> getCartes() {
        return cartes;
    }

    public void setCartes(List<Carte> cartes) {
        this.cartes = cartes;
    }
}
