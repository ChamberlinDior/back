package com.bus.trans.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class VehiculeInterurbain extends Vehicule {

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private double capaciteVolume; // En cm³

    @Column(nullable = false)
    private double capacitePoids;  // En kg

    // Getters et Setters

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public double getCapaciteVolume() {
        return capaciteVolume;
    }

    public void setCapaciteVolume(double capaciteVolume) {
        this.capaciteVolume = capaciteVolume;
    }

    public double getCapacitePoids() {
        return capacitePoids;
    }

    public void setCapacitePoids(double capacitePoids) {
        this.capacitePoids = capacitePoids;
    }
}
