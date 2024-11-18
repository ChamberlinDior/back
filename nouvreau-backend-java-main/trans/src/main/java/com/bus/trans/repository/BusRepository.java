package com.bus.trans.repository;


import com.bus.trans.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    // Recherche d'un bus par l'adresse MAC
    Bus findByMacAddress(String macAddress);
}