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

    public List<LigneTrajetInterurbain> getInterurbainLignesByDateAndDestination(LocalDate date, String destination) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();
        return ligneTrajetRepository.findInterurbainByDestinationAndDate(destination, startDateTime, endDateTime);
    }

    public List<LocalDate> getAvailableDates() {
        List<LigneTrajetInterurbain> trajets = ligneTrajetRepository.findAllInterurbain();
        return trajets.stream()
                .map(trajet -> trajet.getHeureDepart().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAvailableDestinationsByDate(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();
        List<LigneTrajetInterurbain> trajets = ligneTrajetRepository.findInterurbainByDate(startDateTime, endDateTime);
        return trajets.stream()
                .map(LigneTrajetInterurbain::getVille)
                .distinct()
                .collect(Collectors.toList());
    }
}
