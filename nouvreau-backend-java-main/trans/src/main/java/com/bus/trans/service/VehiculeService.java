package com.bus.trans.service;

import com.bus.trans.model.Vehicule;
import com.bus.trans.model.VehiculeInterurbain;
import com.bus.trans.model.VehiculeUrbain;
import com.bus.trans.repository.VehiculeRepository;
import com.bus.trans.repository.VehiculeUrbainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private VehiculeUrbainRepository vehiculeUrbainRepository;

    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    public Vehicule saveVehicule(Vehicule vehicule) {
        if (vehicule instanceof VehiculeUrbain) {
            return vehiculeUrbainRepository.save((VehiculeUrbain) vehicule);
        } else if (vehicule instanceof VehiculeInterurbain) {
            return vehiculeRepository.save(vehicule);
        } else {
            // Gérer d'autres types de véhicules si nécessaire
            return vehiculeRepository.save(vehicule);
        }
    }

    public Vehicule getVehiculeByImmatriculation(String immatriculation) {
        return vehiculeRepository.findByImmatriculation(immatriculation);
    }

    public VehiculeUrbain getVehiculeUrbainByMacAddress(String macAddress) {
        return vehiculeUrbainRepository.findByMacAddress(macAddress);
    }

    public Vehicule getVehiculeById(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    public void deleteVehicule(Long id) {
        vehiculeRepository.deleteById(id);
    }

    public List<VehiculeInterurbain> getInterurbainVehicles() {
        return vehiculeRepository.findAll().stream()
                .filter(v -> v instanceof VehiculeInterurbain)
                .map(v -> (VehiculeInterurbain) v)
                .collect(Collectors.toList());
    }

    public List<VehiculeUrbain> getUrbainVehicles() {
        return vehiculeRepository.findAll().stream()
                .filter(v -> v instanceof VehiculeUrbain)
                .map(v -> (VehiculeUrbain) v)
                .collect(Collectors.toList());
    }
}
