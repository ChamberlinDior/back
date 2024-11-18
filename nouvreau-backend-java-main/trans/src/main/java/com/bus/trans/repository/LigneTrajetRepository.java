package com.bus.trans.repository;

import com.bus.trans.model.LigneTrajet;
import com.bus.trans.model.LigneTrajetInterurbain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LigneTrajetRepository extends JpaRepository<LigneTrajet, Long> {

    List<LigneTrajet> findByTypeLigne(String typeLigne);

    @Query("SELECT l FROM LigneTrajetInterurbain l")
    List<LigneTrajetInterurbain> findAllInterurbain();

    @Query("SELECT l FROM LigneTrajetInterurbain l WHERE l.heureDepart BETWEEN :startDate AND :endDate")
    List<LigneTrajetInterurbain> findInterurbainByDate(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT l FROM LigneTrajetInterurbain l WHERE l.ville = :destination AND l.heureDepart BETWEEN :startDate AND :endDate")
    List<LigneTrajetInterurbain> findInterurbainByDestinationAndDate(String destination, LocalDateTime startDate, LocalDateTime endDate);
}
