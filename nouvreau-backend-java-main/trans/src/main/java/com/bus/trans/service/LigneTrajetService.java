package com.bus.trans.service;

import com.bus.trans.model.LigneTrajet;
import com.bus.trans.model.LigneTrajetInterurbain;
import com.bus.trans.repository.LigneTrajetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LigneTrajetService {

    @Autowired
    private LigneTrajetRepository ligneTrajetRepository;

    public List<LigneTrajet> getAllLignes() {
        return ligneTrajetRepository.findAll();
    }

    public LigneTrajet saveLigne(LigneTrajet ligneTrajet) {
        return ligneTrajetRepository.save(ligneTrajet);
    }

    public LigneTrajet getLigneById(Long id) {
        return ligneTrajetRepository.findById(id).orElse(null);
    }

    public void deleteLigne(Long id) {
        ligneTrajetRepository.deleteById(id);
    }

    public List<LigneTrajet> getLignesByType(String typeLigne) {
        return ligneTrajetRepository.findByTypeLigne(typeLigne);
    }

    // Nouvelle méthode pour obtenir toutes les lignes interurbaines
    public List<LigneTrajetInterurbain> getAllInterurbainLignes() {
        return ligneTrajetRepository.findByTypeLigne("INTERURBAIN").stream()
                .filter(ligne -> ligne instanceof LigneTrajetInterurbain)
                .map(ligne -> (LigneTrajetInterurbain) ligne)
                .collect(Collectors.toList());
    }

    // Méthode pour obtenir les lignes interurbaines par date et destination
    public List<LigneTrajetInterurbain> getInterurbainLignesByDateAndDestination(LocalDate date, String destination) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();
        return ligneTrajetRepository.findByTypeLigneAndLieuArriveeAndHeureDepartBetween(
                "INTERURBAIN", destination, startDateTime, endDateTime);
    }

    // Méthode pour obtenir les dates disponibles
    public List<LocalDate> getAvailableDates() {
        return ligneTrajetRepository.findByTypeLigne("INTERURBAIN").stream()
                .map(ligne -> ((LigneTrajetInterurbain) ligne).getHeureDepart().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }

    // Méthode pour obtenir les destinations disponibles par date
    public List<String> getAvailableDestinationsByDate(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();
        List<LigneTrajetInterurbain> trajets = ligneTrajetRepository.findByTypeLigneAndHeureDepartBetween(
                "INTERURBAIN", startDateTime, endDateTime);
        return trajets.stream()
                .map(LigneTrajetInterurbain::getLieuArrivee)
                .distinct()
                .collect(Collectors.toList());
    }
}
