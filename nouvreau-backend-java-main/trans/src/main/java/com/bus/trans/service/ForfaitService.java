package com.bus.trans.service;

import com.bus.trans.dto.ForfaitDTO;
import com.bus.trans.model.Carte;
import com.bus.trans.model.Forfait;
import com.bus.trans.repository.CarteRepository;
import com.bus.trans.repository.ForfaitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ForfaitService {

    @Autowired
    private ForfaitRepository forfaitRepository;

    @Autowired
    private CarteRepository carteRepository;

    // Créer un forfait pour une carte et mettre à jour le statut de la carte
    public Forfait createForfait(ForfaitDTO forfaitDTO) {
        Optional<Carte> carteOpt = carteRepository.findByRfid(forfaitDTO.getRfid());
        if (carteOpt.isEmpty()) {
            throw new IllegalArgumentException("Carte introuvable avec le RFID fourni");
        }

        Carte carte = carteOpt.get();
        Date dateActivation = new Date();
        Date dateExpiration = calculateExpirationDate(forfaitDTO.getTypeForfait(), dateActivation);

        // Créer et sauvegarder le forfait
        Forfait forfait = new Forfait(forfaitDTO.getTypeForfait(), dateActivation, dateExpiration, carte);
        forfaitRepository.save(forfait);

        // Mise à jour de la carte
        carte.setForfaitActif(true);
        carte.setForfaitExpiration(dateExpiration);
        carteRepository.save(carte);

        return forfait;
    }

    // Récupérer l'historique des forfaits d'une carte
    public List<ForfaitDTO> getForfaitHistory(Long carteId) {
        List<Forfait> forfaits = forfaitRepository.findByCarteId(carteId);
        List<ForfaitDTO> forfaitDTOs = new ArrayList<>();

        for (Forfait forfait : forfaits) {
            ForfaitDTO dto = new ForfaitDTO();
            dto.setTypeForfait(forfait.getTypeForfait());
            dto.setDateExpiration(forfait.getDateExpiration());
            dto.setRfid(forfait.getCarte().getRfid());
            dto.setActif(forfait.getCarte().isForfaitActif());
            forfaitDTOs.add(dto);
        }
        return forfaitDTOs;
    }

    // Supprimer un forfait par ID
    public void deleteForfait(Long id) {
        Optional<Forfait> forfaitOpt = forfaitRepository.findById(id);
        if (forfaitOpt.isPresent()) {
            Forfait forfait = forfaitOpt.get();
            Carte carte = forfait.getCarte();

            // Mettre à jour le statut du forfait sur la carte
            carte.setForfaitActif(false);
            carte.setForfaitExpiration(null);
            carteRepository.save(carte);

            // Supprimer le forfait
            forfaitRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Forfait introuvable avec l'ID fourni");
        }
    }

    // Récupérer le statut du forfait via RFID
    public ForfaitDTO getForfaitStatusByRFID(String rfid) {
        Optional<Carte> carteOpt = carteRepository.findByRfid(rfid);
        if (carteOpt.isEmpty()) {
            return null; // Carte introuvable
        }

        Carte carte = carteOpt.get();
        ForfaitDTO forfaitDTO = new ForfaitDTO();
        forfaitDTO.setRfid(rfid);

        if (carte.isForfaitActif()) {
            Date expirationDate = carte.getForfaitExpiration();
            if (expirationDate != null && expirationDate.after(new Date())) {
                forfaitDTO.setTypeForfait("Actif");
                forfaitDTO.setDateExpiration(expirationDate);
                forfaitDTO.setActif(true);
            } else {
                carte.setForfaitActif(false);
                carte.setForfaitExpiration(null);
                carteRepository.save(carte);
                forfaitDTO.setTypeForfait("Expiré");
                forfaitDTO.setActif(false);
            }
        } else {
            forfaitDTO.setTypeForfait("Inactif");
            forfaitDTO.setActif(false);
        }

        return forfaitDTO;
    }

    // Méthode pour calculer la date d'expiration en fonction du type de forfait
    private Date calculateExpirationDate(String typeForfait, Date dateActivation) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateActivation);
        switch (typeForfait.toLowerCase()) {
            case "jour":
                cal.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case "semaine":
                cal.add(Calendar.DAY_OF_MONTH, 7);
                break;
            case "mois":
                cal.add(Calendar.MONTH, 1);
                break;
            default:
                throw new IllegalArgumentException("Type de forfait invalide: " + typeForfait);
        }
        return cal.getTime();
    }

    // Mettre à jour un forfait existant
    public Forfait updateForfait(Long id, ForfaitDTO forfaitDTO) {
        Optional<Forfait> forfaitOpt = forfaitRepository.findById(id);
        if (forfaitOpt.isEmpty()) {
            throw new IllegalArgumentException("Forfait introuvable avec l'ID fourni");
        }

        Forfait forfait = forfaitOpt.get();
        Carte carte = forfait.getCarte();

        Date dateActivation = new Date();
        Date dateExpiration = calculateExpirationDate(forfaitDTO.getTypeForfait(), dateActivation);

        // Mise à jour des informations du forfait
        forfait.setTypeForfait(forfaitDTO.getTypeForfait());
        forfait.setDateActivation(dateActivation);
        forfait.setDateExpiration(dateExpiration);
        forfaitRepository.save(forfait);

        // Mise à jour de la carte
        carte.setForfaitActif(true);
        carte.setForfaitExpiration(dateExpiration);
        carteRepository.save(carte);

        return forfait;
    }
}
