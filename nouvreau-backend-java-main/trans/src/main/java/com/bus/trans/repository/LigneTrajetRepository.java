package com.bus.trans.repository;


import com.bus.trans.model.LigneTrajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneTrajetRepository extends JpaRepository<LigneTrajet, Long> {
}
