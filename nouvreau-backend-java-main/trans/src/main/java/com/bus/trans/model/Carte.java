package com.bus.trans.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Carte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rfid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExpiration;

    @Column(nullable = false)
    private String nomAgent;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private boolean forfaitActif;

    @Temporal(TemporalType.TIMESTAMP)
    private Date forfaitExpiration;

    public Carte() {
        this.rfid = "RFID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nomAgent = "Agent-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        this.dateCreation = new Date();
        this.active = true;
        this.forfaitActif = false;
    }

    // Getters et Setters pour toutes les propriétés

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isForfaitActif() {
        return forfaitActif;
    }

    public void setForfaitActif(boolean forfaitActif) {
        this.forfaitActif = forfaitActif;
    }

    public Date getForfaitExpiration() {
        return forfaitExpiration;
    }

    public void setForfaitExpiration(Date forfaitExpiration) {
        this.forfaitExpiration = forfaitExpiration;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "id=" + id +
                ", rfid='" + rfid + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateExpiration=" + dateExpiration +
                ", nomAgent='" + nomAgent + '\'' +
                ", active=" + active +
                ", forfaitActif=" + forfaitActif +
                ", forfaitExpiration=" + forfaitExpiration +
                '}';
    }
}
