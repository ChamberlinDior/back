package com.bus.trans.repository;


import com.bus.trans.model.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {
    Terminal findByMacAddress(String macAddress);
}