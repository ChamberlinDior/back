package com.bus.trans.dto;

public class VehiculeInterurbainDTO extends VehiculeDTO {

    private int capacite;
    private double capaciteVolume; // En cm³
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
