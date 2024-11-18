package com.bus.trans.repository;

import com.bus.trans.model.LigneTrajet;
import com.bus.trans.model.LigneTrajetInterurbain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LigneTrajetRepository extends JpaRepository<LigneTrajet, Long> {

    List<LigneTrajet> findByTypeLigne(String typeLigne);

    // Méthode pour trouver les lignes interurbaines par destination et date
    List<LigneTrajetInterurbain> findByTypeLigneAndLieuArriveeAndHeureDepartBetween(
            String typeLigne, String lieuArrivee, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Méthode pour trouver les lignes interurbaines entre deux dates
    List<LigneTrajetInterurbain> findByTypeLigneAndHeureDepartBetween(
            String typeLigne, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
