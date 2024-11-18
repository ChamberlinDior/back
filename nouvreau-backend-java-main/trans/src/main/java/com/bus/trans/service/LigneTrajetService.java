package com.bus.trans.service;


import com.bus.trans.model.LigneTrajet;
import com.bus.trans.repository.LigneTrajetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LigneTrajetService {

    @Autowired
    private LigneTrajetRepository ligneTrajetRepository;

    public List<LigneTrajet> getAllLignes() {
        return ligneTrajetRepository.findAll();
    }

    public LigneTrajet saveLigne(LigneTrajet ligneTrajet) {
        return ligneTrajetRepository.save(ligneTrajet);
    }

    public LigneTrajet getLigneById(Long id) {
        return ligneTrajetRepository.findById(id).orElse(null);
    }

    public void deleteLigne(Long id) {
        ligneTrajetRepository.deleteById(id);
    }
}
