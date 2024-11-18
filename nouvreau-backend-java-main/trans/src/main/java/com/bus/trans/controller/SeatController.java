package com.bus.trans.controller;

import com.bus.trans.model.Seat;
import com.bus.trans.model.VehiculeInterurbain;
import com.bus.trans.service.SeatService;
import com.bus.trans.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private VehiculeService vehiculeService;

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<?> getSeatsByVehicule(@PathVariable Long vehiculeId) {
        VehiculeInterurbain vehicule = (VehiculeInterurbain) vehiculeService.getVehiculeById(vehiculeId);
        if (vehicule == null) {
            return ResponseEntity.badRequest().body("Véhicule interurbain non trouvé.");
        }
        List<Seat> seats = seatService.getSeatsByVehicule(vehicule);
        return ResponseEntity.ok(seats);
    }
}
