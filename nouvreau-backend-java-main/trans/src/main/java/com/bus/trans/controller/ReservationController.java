package com.bus.trans.controller;

import com.bus.trans.dto.ReservationDTO;
import com.bus.trans.model.*;
import com.bus.trans.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LigneTrajetService ligneTrajetService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private VehiculeService vehiculeService;

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            LigneTrajetInterurbain trajet = (LigneTrajetInterurbain) ligneTrajetService.getLigneById(reservationDTO.getTrajetId());
            if (trajet == null) {
                return ResponseEntity.badRequest().body("Trajet non trouvé.");
            }

            Seat seat = seatService.getSeatById(reservationDTO.getSeatId());
            if (seat == null) {
                return ResponseEntity.badRequest().body("Place non trouvée.");
            }

            // Vérifier si la place est déjà réservée pour ce trajet
            List<Reservation> existingReservations = reservationService.getReservationsBySeatAndTrajet(seat, trajet);
            if (!existingReservations.isEmpty()) {
                return ResponseEntity.badRequest().body("Cette place est déjà réservée pour ce trajet.");
            }

            Passager passager;
            if (reservationDTO.getCarteClient() != null) {
                passager = reservationService.getPassagerByCarteClient(reservationDTO.getCarteClient());
                if (passager == null) {
                    return ResponseEntity.badRequest().body("Aucun passager trouvé avec cette carte client.");
                }
            } else {
                passager = new Passager();
                passager.setNom(reservationDTO.getNom());
                passager.setPrenom(reservationDTO.getPrenom());
                passager.setDateNaissance(reservationDTO.getDateNaissance());
                passager = reservationService.savePassager(passager);
            }

            Reservation reservation = new Reservation();
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setSeat(seat);
            reservation.setTrajet(trajet);
            reservation.setPassager(passager);

            Reservation newReservation = reservationService.createReservation(reservation);

            // Générer le titre de transport (ici, vous pouvez implémenter la génération du PDF)
            // ...

            return ResponseEntity.ok(newReservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<?> getReservationsByTrajet(@PathVariable Long trajetId) {
        LigneTrajetInterurbain trajet = (LigneTrajetInterurbain) ligneTrajetService.getLigneById(trajetId);
        if (trajet == null) {
            return ResponseEntity.badRequest().body("Trajet non trouvé.");
        }
        List<Reservation> reservations = reservationService.getReservationsByTrajet(trajet);
        return ResponseEntity.ok(reservations);
    }
}
