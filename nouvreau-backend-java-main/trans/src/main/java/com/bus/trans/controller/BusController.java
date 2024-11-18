package com.bus.trans.controller;

import com.bus.trans.model.Bus;
import com.bus.trans.model.BusChangeLog;
import com.bus.trans.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    @Autowired
    private BusService busService;

    // Récupérer tous les bus
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        List<Bus> buses = busService.getAllBuses();
        return ResponseEntity.ok(buses);
    }

    // Récupérer un bus par son ID
    @GetMapping("/{busId}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long busId) {
        Bus bus = busService.getBusById(busId);
        return bus != null ? ResponseEntity.ok(bus) : ResponseEntity.notFound().build();
    }

    // Récupérer un bus par son adresse MAC
    @GetMapping("/mac/{macAddress}")
    public ResponseEntity<Bus> getBusByMacAddress(@PathVariable String macAddress) {
        Bus bus = busService.getBusByMacAddress(macAddress);
        return bus != null ? ResponseEntity.ok(bus) : ResponseEntity.notFound().build();
    }

    // Créer un nouveau bus
    @PostMapping("/create")
    public ResponseEntity<?> createBus(@RequestBody Bus bus) {
        try {
            Bus newBus = busService.saveBus(bus);
            return ResponseEntity.ok(newBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du bus : " + e.getMessage());
        }
    }

    // Mise à jour du matricule d'un bus
    @PatchMapping("/{busId}/matricule")
    public ResponseEntity<?> updateMatricule(@PathVariable Long busId, @RequestParam String matricule) {
        try {
            Bus updatedBus = busService.updateMatricule(busId, matricule);
            return ResponseEntity.ok(updatedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du matricule : " + e.getMessage());
        }
    }

    // Mise à jour du modèle d'un bus
    @PatchMapping("/{busId}/modele")
    public ResponseEntity<?> updateModele(@PathVariable Long busId, @RequestParam String modele) {
        try {
            Bus updatedBus = busService.updateModele(busId, modele);
            return ResponseEntity.ok(updatedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du modèle : " + e.getMessage());
        }
    }

    // Mise à jour de la marque d'un bus
    @PatchMapping("/{busId}/marque")
    public ResponseEntity<?> updateMarque(@PathVariable Long busId, @RequestParam String marque) {
        try {
            Bus updatedBus = busService.updateMarque(busId, marque);
            return ResponseEntity.ok(updatedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la marque : " + e.getMessage());
        }
    }

    // Mise à jour de l'année de création d'un bus
    @PatchMapping("/{busId}/anneeCreation")
    public ResponseEntity<?> updateAnneeCreation(@PathVariable Long busId, @RequestParam String anneeCreation) {
        try {
            Bus updatedBus = busService.updateAnneeCreation(busId, anneeCreation);
            return ResponseEntity.ok(updatedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de l'année de création : " + e.getMessage());
        }
    }

    // Mettre à jour le chauffeur et la destination par adresse MAC
    @PostMapping("/mac/{macAddress}/update-chauffeur-destination")
    public ResponseEntity<?> updateChauffeurAndDestinationByMac(
            @PathVariable String macAddress,
            @RequestParam String lastDestination,
            @RequestParam String chauffeurNom,
            @RequestParam String chauffeurUniqueNumber) {
        Bus bus = busService.updateChauffeurAndDestinationByMacAddress(macAddress, lastDestination, chauffeurNom, chauffeurUniqueNumber);
        return bus != null ? ResponseEntity.ok("Chauffeur et destination mis à jour avec succès.") : ResponseEntity.notFound().build();
    }

    // Démarrer un trajet par adresse MAC
    @PostMapping("/mac/{macAddress}/start-trip")
    public ResponseEntity<?> startTrip(@PathVariable String macAddress, @RequestParam String lastDestination) {
        Bus bus = busService.startTrip(macAddress, lastDestination);
        return bus != null ? ResponseEntity.ok("Trajet démarré avec succès.") : ResponseEntity.notFound().build();
    }

    // Terminer un trajet par adresse MAC
    @PostMapping("/mac/{macAddress}/end-trip")
    public ResponseEntity<?> endTrip(@PathVariable String macAddress) {
        Bus bus = busService.endTrip(macAddress);
        return bus != null ? ResponseEntity.ok("Trajet terminé avec succès.") : ResponseEntity.notFound().build();
    }

    // Mettre à jour le niveau de batterie et l'état de charge
    @PostMapping("/mac/{macAddress}/update-battery")
    public ResponseEntity<?> updateBusBatteryLevel(
            @PathVariable String macAddress,
            @RequestParam Integer niveauBatterie,
            @RequestParam boolean isCharging) {
        Bus bus = busService.updateBatteryLevel(macAddress, niveauBatterie, isCharging);
        return bus != null ? ResponseEntity.ok("Niveau de batterie et état de charge mis à jour avec succès.") : ResponseEntity.notFound().build();
    }

    // Récupérer l'historique des changements de chauffeur et de destination
    @GetMapping("/mac/{macAddress}/history")
    public ResponseEntity<List<BusChangeLog>> getBusChangeLogByMac(@PathVariable String macAddress) {
        List<BusChangeLog> logs = busService.getBusChangeLogByMacAddress(macAddress);
        return ResponseEntity.ok(logs);
    }
}
