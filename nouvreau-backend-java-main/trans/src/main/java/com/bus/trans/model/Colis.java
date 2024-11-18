package com.bus.trans.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Colis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codeSuivi; // Code de suivi unique

    private double longueur; // en cm
    private double largeur;  // en cm
    private double hauteur;  // en cm
    private double poids;    // en kg
    private double volume;   // en cm³, calculé à partir des dimensions

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private LigneTrajetInterurbain trajet;

    @ManyToOne
    @JoinColumn(name = "passager_id", nullable = false)
    private Passager passager; // Association avec le passager

    private LocalDateTime dateEnvoi;

    @Enumerated(EnumType.STRING)
    private StatutColis statut; // Statut du colis

    // Constructeur par défaut
    public Colis() {
        this.statut = StatutColis.EN_ATTENTE_DEPART;
    }

    // Méthode pour calculer le volume
    public void calculerVolume() {
        this.volume = longueur * largeur * hauteur;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public String getCodeSuivi() {
        return codeSuivi;
    }

    public void setCodeSuivi(String codeSuivi) {
        this.codeSuivi = codeSuivi;
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(double longueur) {
        this.longueur = longueur;
        calculerVolume();
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
        calculerVolume();
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
        calculerVolume();
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public double getVolume() {
        return volume;
    }

    public LigneTrajetInterurbain getTrajet() {
        return trajet;
    }

    public void setTrajet(LigneTrajetInterurbain trajet) {
        this.trajet = trajet;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public StatutColis getStatut() {
        return statut;
    }

    public void setStatut(StatutColis statut) {
        this.statut = statut;
    }
}
