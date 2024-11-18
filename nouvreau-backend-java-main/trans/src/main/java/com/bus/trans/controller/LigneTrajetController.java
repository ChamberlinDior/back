package com.bus.trans.controller;

import com.bus.trans.dto.LigneTrajetInterurbainDTO;
import com.bus.trans.dto.LigneTrajetUrbainDTO;
import com.bus.trans.model.*;
import com.bus.trans.service.LigneTrajetService;
import com.bus.trans.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lignes")
public class LigneTrajetController {

    @Autowired
    private LigneTrajetService ligneTrajetService;

    @Autowired
    private VehiculeService vehiculeService;

    @GetMapping
    public ResponseEntity<List<LigneTrajet>> getAllLignes() {
        return ResponseEntity.ok(ligneTrajetService.getAllLignes());
    }

    @GetMapping("/type/{typeLigne}")
    public ResponseEntity<List<LigneTrajet>> getLignesByType(@PathVariable String typeLigne) {
        return ResponseEntity.ok(ligneTrajetService.getLignesByType(typeLigne));
    }

    @PostMapping("/interurbain/create")
    public ResponseEntity<?> createInterurbainLigne(@RequestBody LigneTrajetInterurbainDTO interurbainDTO) {
        try {
            if (interurbainDTO.getLieuDepart() == null || interurbainDTO.getLieuArrivee() == null || interurbainDTO.getMontant() == null) {
                return ResponseEntity.badRequest().body("Les trajets interurbains doivent avoir un lieu de départ, un lieu d'arrivée, et un montant.");
            }

            VehiculeInterurbain vehicule = (VehiculeInterurbain) vehiculeService.getVehiculeById(interurbainDTO.getVehiculeId());
            if (vehicule == null) {
                return ResponseEntity.badRequest().body("Véhicule interurbain non trouvé.");
            }

            LigneTrajetInterurbain interurbain = new LigneTrajetInterurbain();
            interurbain.setNomLigne(interurbainDTO.getNomLigne());
            interurbain.setTypeLigne("INTERURBAIN");
            interurbain.setVille(interurbainDTO.getVille());
            interurbain.setLieuDepart(interurbainDTO.getLieuDepart());
            interurbain.setLieuArrivee(interurbainDTO.getLieuArrivee());
            interurbain.setMontant(interurbainDTO.getMontant());
            interurbain.setHeureDepart(interurbainDTO.getHeureDepart());
            interurbain.setHeureArrivee(interurbainDTO.getHeureArrivee());
            interurbain.setVehicule(vehicule);

            LigneTrajet newLigne = ligneTrajetService.saveLigne(interurbain);
            return ResponseEntity.ok(newLigne);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du trajet interurbain : " + e.getMessage());
        }
    }

    @PostMapping("/urbain/create")
    public ResponseEntity<?> createUrbainLigne(@RequestBody LigneTrajetUrbainDTO urbainDTO) {
        try {
            VehiculeUrbain vehicule = (VehiculeUrbain) vehiculeService.getVehiculeById(urbainDTO.getVehiculeId());
            if (vehicule == null) {
                return ResponseEntity.badRequest().body("Véhicule urbain non trouvé.");
            }

            LigneTrajetUrbain urbain = new LigneTrajetUrbain();
            urbain.setNomLigne(urbainDTO.getNomLigne());
            urbain.setTypeLigne("URBAIN");
            urbain.setVille(urbainDTO.getVille());
            urbain.setVehicule(vehicule);

            LigneTrajet newLigne = ligneTrajetService.saveLigne(urbain);
            return ResponseEntity.ok(newLigne);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du trajet urbain : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneTrajet> getLigneById(@PathVariable Long id) {
        LigneTrajet ligneTrajet = ligneTrajetService.getLigneById(id);
        if (ligneTrajet != null) {
            return ResponseEntity.ok(ligneTrajet);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLigne(@PathVariable Long id) {
        try {
            ligneTrajetService.deleteLigne(id);
            return ResponseEntity.ok("Ligne supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression de la ligne : " + e.getMessage());
        }
    }
}
