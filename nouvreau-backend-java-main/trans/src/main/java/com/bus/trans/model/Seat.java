package com.bus.trans.model;

import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seatNumber; // Numéro de la place

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private VehiculeInterurbain vehicule;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public VehiculeInterurbain getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeInterurbain vehicule) {
        this.vehicule = vehicule;
    }
}
