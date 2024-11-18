package com.bus.trans.repository;

import com.bus.trans.model.Colis;
import com.bus.trans.model.LigneTrajetInterurbain;
import com.bus.trans.model.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, Long> {
    List<Colis> findByTrajet(LigneTrajetInterurbain trajet);
    List<Colis> findByPassager(Passager passager);
}
