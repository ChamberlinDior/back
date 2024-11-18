package com.bus.trans.controller;

import com.bus.trans.dto.ColisDTO;
import com.bus.trans.model.Colis;
import com.bus.trans.model.LigneTrajetInterurbain;
import com.bus.trans.model.Passager;
import com.bus.trans.service.ColisService;
import com.bus.trans.service.LigneTrajetService;
import com.bus.trans.service.PassagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/colis")
public class ColisController {

    @Autowired
    private ColisService colisService;

    @Autowired
    private LigneTrajetService ligneTrajetService;

    @Autowired
    private PassagerService passagerService;

    @PostMapping("/create")
    public ResponseEntity<?> createColis(@RequestBody ColisDTO colisDTO) {
        try {
            LigneTrajetInterurbain trajet = (LigneTrajetInterurbain) ligneTrajetService.getLigneById(colisDTO.getTrajetId());
            if (trajet == null) {
                return ResponseEntity.badRequest().body("Trajet non trouvé.");
            }

            Passager passager = passagerService.getPassagerById(colisDTO.getPassagerId());
            if (passager == null) {
                return ResponseEntity.badRequest().body("Passager non trouvé.");
            }

            // Générer un code de suivi unique
            String codeSuivi = UUID.randomUUID().toString();

            Colis colis = new Colis();
            colis.setLongueur(colisDTO.getLongueur());
            colis.setLargeur(colisDTO.getLargeur());
            colis.setHauteur(colisDTO.getHauteur());
            colis.setPoids(colisDTO.getPoids());
            colis.calculerVolume();
            colis.setCodeSuivi(codeSuivi);
            colis.setTrajet(trajet);
            colis.setDateEnvoi(colisDTO.getDateEnvoi() != null ? colisDTO.getDateEnvoi() : java.time.LocalDateTime.now());
            colis.setPassager(passager);

            // Enregistrer le colis
            Colis savedColis = colisService.saveColis(colis);

            // Générer la facture
            generateInvoice(savedColis);

            // Retourner le ColisDTO avec les informations mises à jour
            ColisDTO savedColisDTO = mapToDTO(savedColis);

            return ResponseEntity.ok(savedColisDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du colis : " + e.getMessage());
        }
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<?> getColisByTrajet(@PathVariable Long trajetId) {
        LigneTrajetInterurbain trajet = (LigneTrajetInterurbain) ligneTrajetService.getLigneById(trajetId);
        if (trajet == null) {
            return ResponseEntity.badRequest().body("Trajet non trouvé.");
        }
        List<Colis> colisList = colisService.getColisByTrajet(trajet);
        List<ColisDTO> colisDTOList = colisList.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(colisDTOList);
    }

    @GetMapping("/tracking")
    public ResponseEntity<?> getAllColisWithDetails() {
        List<Colis> colisList = colisService.getAllColis();
        List<ColisDTO> colisDTOList = colisList.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(colisDTOList);
    }

    @GetMapping("/reprint/label/{colisId}")
    public ResponseEntity<?> reprintLabel(@PathVariable Long colisId) {
        Colis colis = colisService.getColisById(colisId);
        if (colis == null) {
            return ResponseEntity.badRequest().body("Colis non trouvé.");
        }
        try {
            generateColisLabel(colis);
            return ResponseEntity.ok("Étiquette rééditée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la réédition de l'étiquette : " + e.getMessage());
        }
    }

    @GetMapping("/reprint/invoice/{colisId}")
    public ResponseEntity<?> reprintInvoice(@PathVariable Long colisId) {
        Colis colis = colisService.getColisById(colisId);
        if (colis == null) {
            return ResponseEntity.badRequest().body("Colis non trouvé.");
        }
        try {
            generateInvoice(colis);
            return ResponseEntity.ok("Facture rééditée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la réédition de la facture : " + e.getMessage());
        }
    }

    private ColisDTO mapToDTO(Colis colis) {
        ColisDTO dto = new ColisDTO();
        dto.setId(colis.getId());
        dto.setCodeSuivi(colis.getCodeSuivi());
        dto.setLongueur(colis.getLongueur());
        dto.setLargeur(colis.getLargeur());
        dto.setHauteur(colis.getHauteur());
        dto.setPoids(colis.getPoids());
        dto.setVolume(colis.getVolume());
        dto.setTrajetId(colis.getTrajet().getId());
        dto.setPassagerId(colis.getPassager().getId());
        dto.setDateEnvoi(colis.getDateEnvoi());
        dto.setStatut(colis.getStatut().toString());
        return dto;
    }

    // Méthode pour générer la facture
    private void generateInvoice(Colis colis) throws Exception {
        // Chemin où le PDF sera enregistré
        String filePath = "invoices/invoice_" + colis.getCodeSuivi() + ".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Ajouter le contenu de la facture
        document.add(new Paragraph("Facture d'Expédition", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph(" ")); // Ligne vide

        document.add(new Paragraph("Code de suivi : " + colis.getCodeSuivi()));
        document.add(new Paragraph("Date d'envoi : " + colis.getDateEnvoi().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        // Informations du passager
        Passager passager = colis.getPassager();
        document.add(new Paragraph("Nom du client : " + passager.getNom() + " " + passager.getPrenom()));

        // Informations du voyage
        LigneTrajetInterurbain trajet = colis.getTrajet();
        document.add(new Paragraph("Voyage : " + trajet.getNomLigne()));
        document.add(new Paragraph("Départ : " + trajet.getLieuDepart() + " à " + trajet.getHeureDepart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        document.add(new Paragraph("Arrivée : " + trajet.getLieuArrivee() + " à " + trajet.getHeureArrivee().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        // Informations du colis
        document.add(new Paragraph("Dimensions : " + colis.getLongueur() + " x " + colis.getLargeur() + " x " + colis.getHauteur() + " cm"));
        document.add(new Paragraph("Poids : " + colis.getPoids() + " kg"));

        // Montant à payer (vous pouvez ajuster le calcul)
        double montant = calculerMontant(colis);
        document.add(new Paragraph("Montant à payer : " + montant + " EUR"));

        document.close();
    }

    private double calculerMontant(Colis colis) {
        // Exemple simple : montant basé sur le poids et le volume
        double tarifParKg = 2.0; // Tarif par kg
        double tarifParCm3 = 0.0001; // Tarif par cm³

        double montantPoids = colis.getPoids() * tarifParKg;
        double montantVolume = colis.getVolume() * tarifParCm3;

        return montantPoids + montantVolume;
    }

    // Méthode pour générer l'étiquette du colis
    private void generateColisLabel(Colis colis) throws Exception {
        // Chemin où le PDF sera enregistré
        String filePath = "labels/label_" + colis.getCodeSuivi() + ".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Ajouter le contenu de l'étiquette
        document.add(new Paragraph("Étiquette du Colis", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph(" ")); // Ligne vide

        document.add(new Paragraph("Code de suivi : " + colis.getCodeSuivi()));
        document.add(new Paragraph("Date d'envoi : " + colis.getDateEnvoi().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        // Informations du voyage
        LigneTrajetInterurbain trajet = colis.getTrajet();
        document.add(new Paragraph("Départ : " + trajet.getLieuDepart()));
        document.add(new Paragraph("Arrivée : " + trajet.getLieuArrivee()));

        // Informations du colis
        document.add(new Paragraph("Dimensions : " + colis.getLongueur() + " x " + colis.getLargeur() + " x " + colis.getHauteur() + " cm"));
        document.add(new Paragraph("Poids : " + colis.getPoids() + " kg"));

        // Génération d'un QR code (simulation)
        document.add(new Paragraph("QR Code : [QR CODE SIMULÉ]"));

        document.close();
    }
}
