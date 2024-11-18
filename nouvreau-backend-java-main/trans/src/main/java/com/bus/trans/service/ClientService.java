package com.bus.trans.service;

import com.bus.trans.model.Carte;
import com.bus.trans.model.Client;
import com.bus.trans.repository.CarteRepository;
import com.bus.trans.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarteRepository carteRepository;

    // Méthode pour récupérer tous les clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Méthode pour créer un nouveau client
    public Client createClient(Client client) {
        if (client.getCartes() != null) {
            for (Carte carte : client.getCartes()) {
                carte.setClient(client);
            }
        }
        return clientRepository.save(client);
    }

    // Méthode pour mettre à jour un client existant
    public Client updateClient(Long id, Client clientDetails) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setNom(clientDetails.getNom());
            client.setPrenom(clientDetails.getPrenom());
            client.setQuartier(clientDetails.getQuartier());
            client.setVille(clientDetails.getVille());
            client.setNomAgent(clientDetails.getNomAgent());

            if (clientDetails.getCartes() != null) {
                for (Carte carte : clientDetails.getCartes()) {
                    carte.setClient(client);
                }
            }
            return clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("Client non trouvé avec l'ID fourni");
        }
    }

    // Méthode pour récupérer un client par ID
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    // Méthode pour récupérer un client par RFID
    public Client getClientByRFID(String rfid) {
        logger.info("Recherche de client par RFID: {}", rfid);
        Optional<Client> clientOpt = clientRepository.findClientByRFID(rfid.toUpperCase().trim());
        if (clientOpt.isPresent()) {
            logger.info("Client trouvé pour RFID: {}", rfid);
            return clientOpt.get();
        } else {
            logger.warn("Aucun client trouvé pour RFID: {}", rfid);
            return null;
        }
    }

    // Méthode pour supprimer un client
    public void deleteClient(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            clientRepository.delete(clientOptional.get());
        } else {
            throw new IllegalArgumentException("Client non trouvé avec l'ID fourni");
        }
    }

    // Méthode pour ajouter une carte à un client
    public Carte addCarteToClient(Long clientId, Carte carte) {
        Client client = getClientById(clientId);
        if (client != null) {
            carte.setClient(client);
            return carteRepository.save(carte);
        } else {
            throw new IllegalArgumentException("Client non trouvé avec l'ID fourni.");
        }
    }

    // Méthode pour mettre à jour une carte existante
    public Carte updateCarte(Long carteId, Carte carteDetails) {
        Optional<Carte> carteOptional = carteRepository.findById(carteId);
        if (carteOptional.isPresent()) {
            Carte carte = carteOptional.get();
            carte.setActive(carteDetails.isActive());
            carte.setDateExpiration(carteDetails.getDateExpiration());
            carte.setClient(carteDetails.getClient());
            return carteRepository.save(carte);
        } else {
            throw new IllegalArgumentException("Carte non trouvée avec l'ID fourni");
        }
    }

    // Méthode pour supprimer une carte
    public void deleteCarte(Long carteId) {
        Optional<Carte> carteOptional = carteRepository.findById(carteId);
        if (carteOptional.isPresent()) {
            carteRepository.delete(carteOptional.get());
        } else {
            throw new IllegalArgumentException("Carte non trouvée avec l'ID fourni");
        }
    }

    // Méthode pour récupérer toutes les cartes
    public List<Carte> getAllCartes() {
        return carteRepository.findAll();
    }

    // Méthode pour récupérer une carte par ID
    public Carte getCarteById(Long carteId) {
        return carteRepository.findById(carteId).orElse(null);
    }
}
