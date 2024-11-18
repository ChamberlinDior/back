package com.bus.trans.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class ForfaitVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private String rfid;

    @Column(nullable = false)
    private String statutForfait;

    @Column(nullable = false)
    private String androidId;  // ID de l'appareil Android

    @Column(nullable = false)
    private String roleUtilisateur;  // Rôle de l'utilisateur

    @Column(nullable = false)
    private String nomUtilisateur;  // Nom de l'utilisateur qui effectue la vérification

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateVerification;

    // **Nouvelle colonne pour savoir si le forfait a été activé par le client**
    @Column(nullable = false)
    private boolean forfaitActiverParClient;

    public ForfaitVerification() {
        this.dateVerification = new Date();  // Initialiser avec la date actuelle
    }

    public ForfaitVerification(String nomClient, String rfid, String statutForfait, String androidId, String roleUtilisateur, String nomUtilisateur, boolean forfaitActiverParClient) {
        this.nomClient = nomClient;
        this.rfid = rfid;
        this.statutForfait = statutForfait;
        this.androidId = androidId;
        this.roleUtilisateur = roleUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.forfaitActiverParClient = forfaitActiverParClient;
        this.dateVerification = new Date();
    }

    // Getters et Setters pour la nouvelle colonne
    public boolean isForfaitActiverParClient() {
        return forfaitActiverParClient;
    }

    public void setForfaitActiverParClient(boolean forfaitActiverParClient) {
        this.forfaitActiverParClient = forfaitActiverParClient;
    }

    // Autres getters et setters inchangés...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getStatutForfait() {
        return statutForfait;
    }

    public void setStatutForfait(String statutForfait) {
        this.statutForfait = statutForfait;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getRoleUtilisateur() {
        return roleUtilisateur;
    }

    public void setRoleUtilisateur(String roleUtilisateur) {
        this.roleUtilisateur = roleUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public Date getDateVerification() {
        return dateVerification;
    }

    public void setDateVerification(Date dateVerification) {
        this.dateVerification = dateVerification;
    }
}
