package com.bus.trans.repository;

import com.bus.trans.model.LigneTrajetInterurbain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LigneTrajetInterurbainRepository extends JpaRepository<LigneTrajetInterurbain, Long> {

    List<LigneTrajetInterurbain> findByVilleAndHeureDepartBetween(String ville, LocalDateTime startDate, LocalDateTime endDate);
}
