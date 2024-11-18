package com.bus.trans.repository;


import com.bus.trans.model.BusChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusChangeLogRepository extends JpaRepository<BusChangeLog, Long> {
    // Recherche des logs par adresse MAC du bus
    List<BusChangeLog> findByBusMacAddress(String macAddress);
}
