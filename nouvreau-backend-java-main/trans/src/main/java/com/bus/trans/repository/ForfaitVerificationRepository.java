package com.bus.trans.repository;

import com.bus.trans.model.ForfaitVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForfaitVerificationRepository extends JpaRepository<ForfaitVerification, Long> {

    // Récupérer les vérifications de forfaits par RFID
    List<ForfaitVerification> findByRfid(String rfid);
}
