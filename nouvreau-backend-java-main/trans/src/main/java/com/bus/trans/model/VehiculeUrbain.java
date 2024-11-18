package com.bus.trans.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class VehiculeUrbain extends Vehicule {

    @Column(nullable = false, unique = true)
    private String macAddress;

    // Getters et Setters
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
