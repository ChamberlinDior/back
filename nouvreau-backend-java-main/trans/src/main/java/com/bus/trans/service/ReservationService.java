package com.bus.trans.service;

import com.bus.trans.model.*;
import com.bus.trans.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PassagerRepository passagerRepository;

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByTrajet(LigneTrajetInterurbain trajet) {
        return reservationRepository.findByTrajet(trajet);
    }

    public List<Reservation> getReservationsBySeatAndTrajet(Seat seat, LigneTrajetInterurbain trajet) {
        return reservationRepository.findBySeatAndTrajet(seat, trajet);
    }

    public Passager getPassagerByCarteClient(String carteClient) {
        return passagerRepository.findByCarteClient(carteClient);
    }

    public Passager savePassager(Passager passager) {
        return passagerRepository.save(passager);
    }
}
