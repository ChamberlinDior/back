package com.bus.trans.service;

import com.bus.trans.model.Utilisateur;
import com.bus.trans.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    public Utilisateur getUtilisateurByRfid(String rfid) {
        return utilisateurRepository.findByRfid(rfid).orElse(null);
    }

    public Utilisateur getUtilisateurByUniqueUserNumber(String uniqueUserNumber) {
        return utilisateurRepository.findByUniqueUserNumber(uniqueUserNumber).orElse(null);
    }

    public Utilisateur loginUtilisateur(String rfid, String uniqueUserNumber) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByRfid(rfid);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            if (utilisateur.getUniqueUserNumber().equalsIgnoreCase(uniqueUserNumber)) {
                return utilisateur;
            }
        }
        return null;
    }

    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = getUtilisateurById(id);
        if (utilisateur != null) {
            utilisateur.setNom(utilisateurDetails.getNom());
            utilisateur.setPrenom(utilisateurDetails.getPrenom());
            utilisateur.setRole(utilisateurDetails.getRole());
            utilisateur.setRfid(utilisateurDetails.getRfid());
            return utilisateurRepository.save(utilisateur);
        }
        return null;
    }

    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
