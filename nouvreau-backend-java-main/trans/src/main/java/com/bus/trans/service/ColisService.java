package com.bus.trans.service;

import com.bus.trans.model.Colis;
import com.bus.trans.model.LigneTrajetInterurbain;
import com.bus.trans.model.Passager;
import com.bus.trans.repository.ColisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColisService {

    @Autowired
    private ColisRepository colisRepository;

    public Colis saveColis(Colis colis) {
        return colisRepository.save(colis);
    }

    public List<Colis> getColisByTrajet(LigneTrajetInterurbain trajet) {
        return colisRepository.findByTrajet(trajet);
    }

    public List<Colis> getColisByPassager(Passager passager) {
        return colisRepository.findByPassager(passager);
    }

    public List<Colis> getAllColis() {
        return colisRepository.findAll();
    }

    public Colis getColisById(Long id) {
        return colisRepository.findById(id).orElse(null);
    }

    public void deleteColis(Long id) {
        colisRepository.deleteById(id);
    }
}
