package com.bus.trans.repository;

import com.bus.trans.model.Carte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteRepository extends JpaRepository<Carte, Long> {
    Optional<Carte> findByRfid(String rfid);
}
