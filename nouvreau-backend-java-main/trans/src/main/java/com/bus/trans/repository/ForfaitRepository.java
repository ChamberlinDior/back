package com.bus.trans.repository;

import com.bus.trans.model.Forfait;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForfaitRepository extends JpaRepository<Forfait, Long> {
    List<Forfait> findByCarteId(Long carteId);  // Récupérer les forfaits par ID de carte
}
