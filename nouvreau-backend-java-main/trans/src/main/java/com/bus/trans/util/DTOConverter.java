package com.bus.trans.util;

import com.bus.trans.dto.CarteDTO;
import com.bus.trans.dto.ClientDTO;
import com.bus.trans.model.Carte;
import com.bus.trans.model.Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Conversion d'un Client vers ClientDTO
    public static ClientDTO convertToClientDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setNumClient(client.getNumClient());
        clientDTO.setNom(client.getNom());
        clientDTO.setPrenom(client.getPrenom());
        clientDTO.setQuartier(client.getQuartier());
        clientDTO.setVille(client.getVille());
        clientDTO.setDateCreation(sdf.format(client.getDateCreation()));
        clientDTO.setNomAgent(client.getNomAgent());
        clientDTO.setCartes(convertToCarteDTOs(client.getCartes())); // Convertir les cartes associées
        return clientDTO;
    }

    // Conversion d'un ClientDTO vers Client
    public static Client convertToClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setNumClient(clientDTO.getNumClient());
        client.setNom(clientDTO.getNom());
        client.setPrenom(clientDTO.getPrenom());
        client.setQuartier(clientDTO.getQuartier());
        client.setVille(clientDTO.getVille());

        // Conversion de la date de création
        try {
            client.setDateCreation(sdf.parse(clientDTO.getDateCreation()));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Erreur de format de la date de création", e);
        }

        client.setNomAgent(clientDTO.getNomAgent());

        List<Carte> cartes = new ArrayList<>();
        if (clientDTO.getCartes() != null) {
            for (CarteDTO carteDTO : clientDTO.getCartes()) {
                Carte carte = convertToCarte(carteDTO);
                carte.setClient(client); // Associer la carte au client
                cartes.add(carte);
            }
        }
        client.setCartes(cartes);

        return client;
    }

    // Conversion d'une liste de Cartes vers CarteDTOs
    public static List<CarteDTO> convertToCarteDTOs(List<Carte> cartes) {
        return cartes.stream().map(DTOConverter::convertToCarteDTO).collect(Collectors.toList());
    }

    // Conversion d'une Carte vers CarteDTO
    public static CarteDTO convertToCarteDTO(Carte carte) {
        CarteDTO carteDTO = new CarteDTO();
        carteDTO.setId(carte.getId());
        carteDTO.setRfid(carte.getRfid());
        carteDTO.setDateCreation(sdf.format(carte.getDateCreation()));
        carteDTO.setDateExpiration(
                carte.getDateExpiration() != null ? sdf.format(carte.getDateExpiration()) : null
        );
        carteDTO.setNomAgent(carte.getNomAgent());
        carteDTO.setActive(carte.isActive());
        carteDTO.setForfaitActif(carte.isForfaitActif());
        carteDTO.setForfaitExpiration(
                carte.getForfaitExpiration() != null ? sdf.format(carte.getForfaitExpiration()) : null
        );
        return carteDTO;
    }

    // Conversion d'un CarteDTO vers Carte
    public static Carte convertToCarte(CarteDTO carteDTO) {
        Carte carte = new Carte();
        carte.setId(carteDTO.getId());
        carte.setRfid(carteDTO.getRfid());

        // Conversion de la date de création
        try {
            carte.setDateCreation(sdf.parse(carteDTO.getDateCreation()));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Erreur de format de la date de création", e);
        }

        // Conversion de la date d'expiration
        if (carteDTO.getDateExpiration() != null && !carteDTO.getDateExpiration().isEmpty()) {
            try {
                carte.setDateExpiration(sdf.parse(carteDTO.getDateExpiration()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Erreur de format de la date d'expiration", e);
            }
        }

        carte.setNomAgent(carteDTO.getNomAgent());
        carte.setActive(carteDTO.isActive());
        carte.setForfaitActif(carteDTO.isForfaitActif());

        // Conversion de la date d'expiration du forfait
        if (carteDTO.getForfaitExpiration() != null && !carteDTO.getForfaitExpiration().isEmpty()) {
            try {
                carte.setForfaitExpiration(sdf.parse(carteDTO.getForfaitExpiration()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Erreur de format de la date d'expiration du forfait", e);
            }
        }

        return carte;
    }

    // Conversion d'une liste de Clients vers ClientDTOs
    public static List<ClientDTO> convertToClientDTOs(List<Client> clients) {
        return clients.stream().map(DTOConverter::convertToClientDTO).collect(Collectors.toList());
    }
}
