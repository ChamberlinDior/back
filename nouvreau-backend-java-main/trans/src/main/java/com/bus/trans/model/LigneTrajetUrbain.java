package com.bus.trans.model;

import jakarta.persistence.*;

@Entity
public class LigneTrajetUrbain extends LigneTrajet {

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private VehiculeUrbain vehicule;

    // Getters et Setters
    public VehiculeUrbain getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeUrbain vehicule) {
        this.vehicule = vehicule;
    }
}
