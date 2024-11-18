package com.bus.trans.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class VehiculeInterurbain extends Vehicule {

    @Column(nullable = false)
    private int capacite;

    // Getters et Setters
    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
}
