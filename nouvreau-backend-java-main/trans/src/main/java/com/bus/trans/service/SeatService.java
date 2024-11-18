package com.bus.trans.service;

import com.bus.trans.model.Seat;
import com.bus.trans.model.Vehicule;
import com.bus.trans.model.VehiculeInterurbain;
import com.bus.trans.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public void generateSeatsForVehicule(VehiculeInterurbain vehicule) {
        int capacite = vehicule.getCapacite();
        for (int i = 1; i <= capacite; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(i);
            seat.setVehicule(vehicule);
            seatRepository.save(seat);
        }
    }

    public Seat getSeatByVehiculeAndNumber(VehiculeInterurbain vehicule, int seatNumber) {
        return seatRepository.findByVehiculeAndSeatNumber(vehicule, seatNumber);
    }

    public List<Seat> getSeatsByVehicule(VehiculeInterurbain vehicule) {
        return seatRepository.findByVehicule(vehicule);
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id).orElse(null);
    }
}
