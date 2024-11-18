package com.bus.trans.controller;

import com.bus.trans.dto.CarteDTO;
import com.bus.trans.dto.ClientDTO;
import com.bus.trans.model.Carte;
import com.bus.trans.model.Client;
import com.bus.trans.service.ClientService;
import com.bus.trans.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Route pour récupérer tous les clients
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        List<ClientDTO> clientDTOs = DTOConverter.convertToClientDTOs(clients);
        return ResponseEntity.ok(clientDTOs);
    }

    // Route pour récupérer un client par ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé");
        }
        return ResponseEntity.ok(DTOConverter.convertToClientDTO(client));
    }

    // Route pour créer un nouveau client
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        Client client = DTOConverter.convertToClient(clientDTO);
        Client newClient = clientService.createClient(client);
        return new ResponseEntity<>(DTOConverter.convertToClientDTO(newClient), HttpStatus.CREATED);
    }

    // Route pour mettre à jour un client
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        try {
            Client updatedClient = clientService.updateClient(id, DTOConverter.convertToClient(clientDTO));
            return ResponseEntity.ok(DTOConverter.convertToClientDTO(updatedClient));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé");
        }
    }

    // Route pour supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé");
        }
    }

    // Route pour récupérer un client par RFID
    @GetMapping("/rfid/{rfid}")
    public ResponseEntity<ClientDTO> getClientByRFID(@PathVariable String rfid) {
        Client client = clientService.getClientByRFID(rfid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé pour ce RFID");
        }
        return ResponseEntity.ok(DTOConverter.convertToClientDTO(client));
    }

    // Route pour récupérer toutes les cartes
    @GetMapping("/cartes")
    public ResponseEntity<List<CarteDTO>> getAllCartes() {
        List<Carte> cartes = clientService.getAllCartes();
        List<CarteDTO> carteDTOs = DTOConverter.convertToCarteDTOs(cartes);
        return ResponseEntity.ok(carteDTOs);
    }

    // Route pour récupérer une carte par ID
    @GetMapping("/cartes/{carteId}")
    public ResponseEntity<CarteDTO> getCarteById(@PathVariable Long carteId) {
        Carte carte = clientService.getCarteById(carteId);
        if (carte == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte non trouvée");
        }
        return ResponseEntity.ok(DTOConverter.convertToCarteDTO(carte));
    }

    // Route pour ajouter une carte à un client
    @PostMapping("/{clientId}/cartes")
    public ResponseEntity<CarteDTO> addCarteToClient(@PathVariable Long clientId, @RequestBody CarteDTO carteDTO) {
        Carte carte = DTOConverter.convertToCarte(carteDTO);
        carte.setClient(clientService.getClientById(clientId));  // Associe le client à la carte
        Carte newCarte = clientService.addCarteToClient(clientId, carte);
        return ResponseEntity.ok(DTOConverter.convertToCarteDTO(newCarte));
    }

    // Route pour mettre à jour une carte
    @PutMapping("/cartes/{carteId}")
    public ResponseEntity<CarteDTO> updateCarte(@PathVariable Long carteId, @RequestBody CarteDTO carteDetails) {
        Carte updatedCarte = clientService.updateCarte(carteId, DTOConverter.convertToCarte(carteDetails));
        if (updatedCarte == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte non trouvée");
        }
        return ResponseEntity.ok(DTOConverter.convertToCarteDTO(updatedCarte));
    }

    // Route pour supprimer une carte
    @DeleteMapping("/cartes/{carteId}")
    public ResponseEntity<Void> deleteCarte(@PathVariable Long carteId) {
        try {
            clientService.deleteCarte(carteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte non trouvée");
        }
    }
}
