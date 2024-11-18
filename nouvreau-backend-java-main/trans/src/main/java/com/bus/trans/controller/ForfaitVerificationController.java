package com.bus.trans.controller;

import com.bus.trans.dto.ForfaitVerificationDTO;
import com.bus.trans.model.ForfaitVerification;
import com.bus.trans.service.ForfaitVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forfait-verifications")
public class ForfaitVerificationController {

    @Autowired
    private ForfaitVerificationService forfaitVerificationService;

    // Enregistrer une nouvelle vérification de forfait
    @PostMapping
    public ResponseEntity<ForfaitVerification> createForfaitVerification(@RequestBody ForfaitVerificationDTO forfaitVerificationDTO) {
        try {
            ForfaitVerification savedVerification = forfaitVerificationService.saveForfaitVerification(forfaitVerificationDTO);
            return ResponseEntity.ok(savedVerification);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Récupérer toutes les vérifications de forfaits
    @GetMapping
    public ResponseEntity<List<ForfaitVerification>> getAllForfaitVerifications() {
        List<ForfaitVerification> verifications = forfaitVerificationService.getAllForfaitVerifications();
        return ResponseEntity.ok(verifications);
    }

    // Récupérer l'historique des vérifications pour un RFID spécifique
    @GetMapping("/rfid/{rfid}")
    public ResponseEntity<List<ForfaitVerification>> getForfaitVerificationsByRfid(@PathVariable String rfid) {
        List<ForfaitVerification> verifications = forfaitVerificationService.getForfaitVerificationsByRfid(rfid);
        if (verifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(verifications);
    }
}
