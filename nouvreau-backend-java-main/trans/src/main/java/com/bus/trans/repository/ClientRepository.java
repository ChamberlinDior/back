package com.bus.trans.repository;

import com.bus.trans.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Recherche d'un client par RFID de la carte associée
    @Query("SELECT c FROM Client c JOIN c.cartes carte WHERE UPPER(carte.rfid) = UPPER(:rfid)")
    Optional<Client> findClientByRFID(@Param("rfid") String rfid);
}
