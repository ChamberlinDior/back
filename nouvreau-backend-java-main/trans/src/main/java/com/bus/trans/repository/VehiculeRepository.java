package com.bus.trans.repository;

import com.bus.trans.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Vehicule findByImmatriculation(String immatriculation);
}
