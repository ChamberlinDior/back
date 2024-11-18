package com.bus.trans.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "forfaits")
public class Forfait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String typeForfait;  // Ex: "jour", "semaine", "mois"

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateActivation;  // Date d'activation du forfait

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateExpiration;  // Date d'expiration du forfait

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carte_id", nullable = false)  // Association avec la carte
    private Carte carte;  // Le forfait est désormais lié à une carte

    // Constructeurs
    public Forfait() {}

    public Forfait(String typeForfait, Date dateActivation, Date dateExpiration, Carte carte) {
        this.typeForfait = typeForfait;
        this.dateActivation = dateActivation;
        this.dateExpiration = dateExpiration;
        this.carte = carte;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeForfait() {
        return typeForfait;
    }

    public void setTypeForfait(String typeForfait) {
        this.typeForfait = typeForfait;
    }

    public Date getDateActivation() {
        return dateActivation;
    }

    public void setDateActivation(Date dateActivation) {
        this.dateActivation = dateActivation;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    @Override
    public String toString() {
        return "Forfait{" +
                "id=" + id +
                ", typeForfait='" + typeForfait + '\'' +
                ", dateActivation=" + dateActivation +
                ", dateExpiration=" + dateExpiration +
                ", carteId=" + (carte != null ? carte.getId() : null) +
                '}';
    }
}
