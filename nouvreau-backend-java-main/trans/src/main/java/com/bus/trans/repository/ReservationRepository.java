package com.bus.trans.repository;

import com.bus.trans.model.Reservation;
import com.bus.trans.model.LigneTrajetInterurbain;
import com.bus.trans.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTrajet(LigneTrajetInterurbain trajet);
    List<Reservation> findBySeatAndTrajet(Seat seat, LigneTrajetInterurbain trajet);
}
