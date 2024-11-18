package com.bus.trans.controller;

import com.bus.trans.dto.VehiculeDTO;
import com.bus.trans.dto.VehiculeInterurbainDTO;
import com.bus.trans.dto.VehiculeUrbainDTO;
import com.bus.trans.model.Vehicule;
import com.bus.trans.model.VehiculeInterurbain;
import com.bus.trans.model.VehiculeUrbain;
import com.bus.trans.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    @GetMapping
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        return ResponseEntity.ok(vehicules);
    }

    @PostMapping("/create")
    public ResponseEntity<Vehicule> createVehicule(@RequestBody VehiculeDTO vehiculeDTO) {
        try {
            Vehicule vehicule;
            if ("URBAIN".equalsIgnoreCase(vehiculeDTO.getTypeVehicule())) {
                VehiculeUrbainDTO urbainDTO = (VehiculeUrbainDTO) vehiculeDTO;
                VehiculeUrbain vehiculeUrbain = new VehiculeUrbain();
                vehiculeUrbain.setImmatriculation(urbainDTO.getImmatriculation());
                vehiculeUrbain.setMarque(urbainDTO.getMarque());
                vehiculeUrbain.setModele(urbainDTO.getModele());
                vehiculeUrbain.setMacAddress(urbainDTO.getMacAddress());
                vehicule = vehiculeService.saveVehicule(vehiculeUrbain);
            } else {
                VehiculeInterurbainDTO interurbainDTO = (VehiculeInterurbainDTO) vehiculeDTO;
                VehiculeInterurbain vehiculeInterurbain = new VehiculeInterurbain();
                vehiculeInterurbain.setImmatriculation(interurbainDTO.getImmatriculation());
                vehiculeInterurbain.setMarque(interurbainDTO.getMarque());
                vehiculeInterurbain.setModele(interurbainDTO.getModele());
                vehiculeInterurbain.setCapacite(interurbainDTO.getCapacite());
                vehicule = vehiculeService.saveVehicule(vehiculeInterurbain);
            }
            return ResponseEntity.ok(vehicule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
