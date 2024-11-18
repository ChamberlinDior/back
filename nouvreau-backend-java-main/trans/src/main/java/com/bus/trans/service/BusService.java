package com.bus.trans.service;

import com.bus.trans.model.Bus;
import com.bus.trans.model.BusChangeLog;
import com.bus.trans.repository.BusRepository;
import com.bus.trans.repository.BusChangeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusChangeLogRepository busChangeLogRepository;

    // Récupérer tous les bus
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // Récupérer un bus par son ID
    public Bus getBusById(Long busId) {
        Bus bus =busRepository.findById(busId).orElse(null);
        System.out.println(bus.toString());
        return bus;
    }

    // Récupérer un bus par son adresse MAC
    public Bus getBusByMacAddress(String macAddress) {
        return busRepository.findByMacAddress(macAddress);
    }

    // Enregistrer un bus
    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    // Mise à jour du matricule
    public Bus updateMatricule(Long busId, String matricule) {
        Bus bus = busRepository.findById(busId).orElseThrow(() ->
                new IllegalArgumentException("Bus introuvable"));
        bus.setMatricule(matricule);
        return busRepository.save(bus);
    }

    // Mise à jour du modèle
    public Bus updateModele(Long busId, String modele) {
        Bus bus = busRepository.findById(busId).orElseThrow(() ->
                new IllegalArgumentException("Bus introuvable"));
        bus.setModele(modele);
        return busRepository.save(bus);
    }

    // Mise à jour de la marque
    public Bus updateMarque(Long busId, String marque) {
        Bus bus = busRepository.findById(busId).orElseThrow(() ->
                new IllegalArgumentException("Bus introuvable"));
        bus.setMarque(marque);
        return busRepository.save(bus);
    }

    // Mise à jour de l'année de création
    public Bus updateAnneeCreation(Long busId, String anneeCreation) {
        Bus bus = busRepository.findById(busId).orElseThrow(() ->
                new IllegalArgumentException("Bus introuvable"));
        bus.setAnneeCreation(anneeCreation);
        return busRepository.save(bus);
    }

    // Mettre à jour le chauffeur et la destination par adresse MAC
    public Bus updateChauffeurAndDestinationByMacAddress(String macAddress, String lastDestination, String chauffeurNom, String chauffeurUniqueNumber) {
        Bus bus = busRepository.findByMacAddress(macAddress);
        if (bus != null) {
            bus.setLastDestination(lastDestination);
            bus.setChauffeurNom(chauffeurNom);
            bus.setChauffeurUniqueNumber(chauffeurUniqueNumber);
            Bus updatedBus = busRepository.save(bus);
            saveChangeLog(macAddress, chauffeurNom, chauffeurUniqueNumber, lastDestination, bus.getDebutTrajet());
            return updatedBus;
        }
        return null;
    }

    // Démarrer un trajet
    public Bus startTrip(String macAddress, String lastDestination) {
        Bus bus = busRepository.findByMacAddress(macAddress);
        if (bus != null) {
            Date now = new Date();
            bus.setDebutTrajet(now);
            bus.setLastDestination(lastDestination);
            Bus updatedBus = busRepository.save(bus);
            saveChangeLog(macAddress, bus.getChauffeurNom(), bus.getChauffeurUniqueNumber(), lastDestination, now);
            return updatedBus;
        }
        return null;
    }

    // Terminer un trajet
    public Bus endTrip(String macAddress) {
        Bus bus = busRepository.findByMacAddress(macAddress);
        if (bus != null) {
            bus.setFinTrajet(new Date());
            return busRepository.save(bus);
        }
        return null;
    }

    // Mettre à jour le niveau de batterie et l'état de charge
    public Bus updateBatteryLevel(String macAddress, Integer niveauBatterie, boolean isCharging) {
        Bus bus = busRepository.findByMacAddress(macAddress);
        if (bus != null) {
            bus.setNiveauBatterie(niveauBatterie);
            bus.setCharging(isCharging);
            return busRepository.save(bus);
        }
        return null;
    }

    // Récupérer les logs d'un bus par son adresse MAC
    public List<BusChangeLog> getBusChangeLogByMacAddress(String macAddress) {
        return busChangeLogRepository.findByBusMacAddress(macAddress);
    }

    // Enregistrer un log de changement
    private void saveChangeLog(String macAddress, String chauffeurNom, String chauffeurUniqueNumber, String destination, Date debutTrajet) {
        BusChangeLog changeLog = new BusChangeLog();
        changeLog.setBusMacAddress(macAddress);
        changeLog.setChauffeurNom(chauffeurNom);
        changeLog.setChauffeurUniqueNumber(chauffeurUniqueNumber);
        changeLog.setDestination(destination);
        changeLog.setDateChange(new Date());
        changeLog.setDebutTrajet(debutTrajet);
        busChangeLogRepository.save(changeLog);
    }
}
