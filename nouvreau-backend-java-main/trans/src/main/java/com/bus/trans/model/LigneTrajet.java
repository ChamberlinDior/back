package com.bus.trans.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LigneTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomLigne;

    @Column(nullable = false)
    private String typeLigne;

    @Column(nullable = false)
    private String ville;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public String getNomLigne() {
        return nomLigne;
    }

    public void setNomLigne(String nomLigne) {
        this.nomLigne = nomLigne;
    }

    public String getTypeLigne() {
        return typeLigne;
    }

    public void setTypeLigne(String typeLigne) {
        this.typeLigne = typeLigne;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
