package com.bus.trans.repository;

import com.bus.trans.model.VehiculeUrbain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeUrbainRepository extends JpaRepository<VehiculeUrbain, Long> {
    VehiculeUrbain findByMacAddress(String macAddress);
}
