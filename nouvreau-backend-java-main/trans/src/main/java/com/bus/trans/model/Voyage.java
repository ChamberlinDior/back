// Voyage.java
package com.bus.trans.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lieuDepart;
    private String lieuArrivee;
    private Date dateDepart;
    private Date dateArrivee;
    private double espaceRestantVolume;
    private double espaceRestantPoids;

    @OneToMany(mappedBy = "voyage")
    private List<com.bus.trans.model.Colis> colisList;

    // Getters, Setters, Constructors

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLieuDepart() { return lieuDepart; }
    public void setLieuDepart(String lieuDepart) { this.lieuDepart = lieuDepart; }

    public String getLieuArrivee() { return lieuArrivee; }
    public void setLieuArrivee(String lieuArrivee) { this.lieuArrivee = lieuArrivee; }

    public Date getDateDepart() { return dateDepart; }
    public void setDateDepart(Date dateDepart) { this.dateDepart = dateDepart; }

    public Date getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(Date dateArrivee) { this.dateArrivee = dateArrivee; }

    public double getEspaceRestantVolume() { return espaceRestantVolume; }
    public void setEspaceRestantVolume(double espaceRestantVolume) { this.espaceRestantVolume = espaceRestantVolume; }

    public double getEspaceRestantPoids() { return espaceRestantPoids; }
    public void setEspaceRestantPoids(double espaceRestantPoids) { this.espaceRestantPoids = espaceRestantPoids; }
}
