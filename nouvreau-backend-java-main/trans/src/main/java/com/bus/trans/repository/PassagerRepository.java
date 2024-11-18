package com.bus.trans.repository;

import com.bus.trans.model.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
    Passager findByCarteClient(String carteClient);
}
