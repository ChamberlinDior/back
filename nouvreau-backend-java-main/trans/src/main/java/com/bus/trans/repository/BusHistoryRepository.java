package com.bus.trans.repository;


import com.bus.trans.model.BusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusHistoryRepository extends JpaRepository<BusHistory, Long> {

    // Méthode pour récupérer l'historique d'un bus par son ID
    List<BusHistory> findByBusId(Long busId);
}
