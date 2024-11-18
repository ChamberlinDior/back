package com.bus.trans.controller;


import com.bus.trans.model.LigneTrajet;
import com.bus.trans.service.LigneTrajetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lignes")
public class LigneTrajetController {

    @Autowired
    private LigneTrajetService ligneTrajetService;

    @GetMapping
    public ResponseEntity<List<LigneTrajet>> getAllLignes() {
        List<LigneTrajet> lignes = ligneTrajetService.getAllLignes();
        return ResponseEntity.ok(lignes);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLigne(@RequestBody LigneTrajet ligneTrajet) {
        try {
            LigneTrajet newLigne = ligneTrajetService.saveLigne(ligneTrajet);
            return ResponseEntity.ok(newLigne);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la ligne : " + e.getMessage());
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
