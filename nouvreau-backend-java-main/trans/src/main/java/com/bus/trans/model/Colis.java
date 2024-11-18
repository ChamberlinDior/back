
package com.bus.trans.model;

import jakarta.persistence.*;


import java.util.Date;

@Entity
public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double longueur;
    private double largeur;
    private double hauteur;
    private double poids;
    private String codeSuivi;
    private String qrCodeData;
    private String statut;
    private Date dateEnregistrement;
    private Date dateDepart;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private com.bus.trans.model.Voyage voyage;

    // Getters, Setters, Constructors

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getLongueur() { return longueur; }
    public void setLongueur(double longueur) { this.longueur = longueur; }

    public double getLargeur() { return largeur; }
    public void setLargeur(double largeur) { this.largeur = largeur; }

    public double getHauteur() { return hauteur; }
    public void setHauteur(double hauteur) { this.hauteur = hauteur; }

    public double getPoids() { return poids; }
    public void setPoids(double poids) { this.poids = poids; }

    public String getCodeSuivi() { return codeSuivi; }
    public void setCodeSuivi(String codeSuivi) { this.codeSuivi = codeSuivi; }

    public String getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Date getDateEnregistrement() { return dateEnregistrement; }
    public void setDateEnregistrement(Date dateEnregistrement) { this.dateEnregistrement = dateEnregistrement; }

    public Date getDateDepart() { return dateDepart; }
    public void setDateDepart(Date dateDepart) { this.dateDepart = dateDepart; }

    public com.bus.trans.model.Voyage getVoyage() { return voyage; }
    public void setVoyage(com.bus.trans.model.Voyage voyage) { this.voyage = voyage; }
}
