package com.bus.trans.repository;

import com.bus.trans.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByRfid(String rfid);
    Optional<Utilisateur> findByUniqueUserNumber(String uniqueUserNumber);
}
