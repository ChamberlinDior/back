package com.bus.trans.service;

import com.bus.trans.model.Passager;
import com.bus.trans.repository.PassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassagerService {

    @Autowired
    private PassagerRepository passagerRepository;

    public Passager getPassagerById(Long id) {
        return passagerRepository.findById(id).orElse(null);
    }

    public Passager savePassager(Passager passager) {
        return passagerRepository.save(passager);
    }

    public Passager getPassagerByCarteClient(String carteClient) {
        return passagerRepository.findByCarteClient(carteClient);
    }
}
