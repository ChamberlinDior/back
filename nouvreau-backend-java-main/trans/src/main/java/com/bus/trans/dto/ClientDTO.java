package com.bus.trans.dto;

import java.util.List;

public class ClientDTO {

    private Long id;
    private String numClient;
    private String nom;
    private String prenom;
    private String quartier;
    private String ville;
    private String dateCreation;
    private String nomAgent;
    private List<CarteDTO> cartes; // Liste des cartes associées

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

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public List<CarteDTO> getCartes() {
        return cartes;
    }

    public void setCartes(List<CarteDTO> cartes) {
        this.cartes = cartes;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", numClient='" + numClient + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", quartier='" + quartier + '\'' +
                ", ville='" + ville + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", nomAgent='" + nomAgent + '\'' +
                ", cartes=" + cartes +
                '}';
    }
}
