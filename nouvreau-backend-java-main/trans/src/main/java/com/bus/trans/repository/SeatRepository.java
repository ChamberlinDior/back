package com.bus.trans.repository;

import com.bus.trans.model.Seat;
import com.bus.trans.model.VehiculeInterurbain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByVehicule(VehiculeInterurbain vehicule);

    Seat findByVehiculeAndSeatNumber(VehiculeInterurbain vehicule, int seatNumber);
}
