package com.bus.trans.controller;

import com.bus.trans.model.Utilisateur;
import com.bus.trans.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PostMapping
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.saveUtilisateur(utilisateur);
    }

    @GetMapping("/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @GetMapping("/rfid/{rfid}")
    public Utilisateur getUtilisateurByRfid(@PathVariable String rfid) {
        return utilisateurService.getUtilisateurByRfid(rfid);
    }

    @GetMapping("/unique/{uniqueUserNumber}")
    public Utilisateur getUtilisateurByUniqueUserNumber(@PathVariable String uniqueUserNumber) {
        return utilisateurService.getUtilisateurByUniqueUserNumber(uniqueUserNumber);
    }

    @GetMapping("/login")
    public Utilisateur loginUtilisateur(@RequestParam String rfid, @RequestParam String uniqueUserNumber) {
        return utilisateurService.loginUtilisateur(rfid, uniqueUserNumber);
    }

    @PutMapping("/{id}")
    public Utilisateur updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails) {
        return utilisateurService.updateUtilisateur(id, utilisateurDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
    }
}
