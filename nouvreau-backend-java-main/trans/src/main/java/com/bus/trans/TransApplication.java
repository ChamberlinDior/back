package com.bus.trans;

import com.bus.trans.model.*;
import com.bus.trans.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Importations pour la génération de PDF
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@EntityScan(basePackages = "com.bus.trans.model")
@SpringBootApplication
public class TransApplication implements CommandLineRunner {

	@Autowired
	private VehiculeService vehiculeService;

	@Autowired
	private LigneTrajetService ligneTrajetService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private SeatService seatService;

	@Autowired
	private ColisService colisService; // Injection du ColisService

	@Autowired
	private PassagerService passagerService; // Injection du PassagerService

	public static void main(String[] args) {
		SpringApplication.run(TransApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Backend is running...");
		updateVehiculeCapaciteVolume(); // Mise à jour des véhicules existants
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\nOptions disponibles : ");
			System.out.println("1. Créer un véhicule");
			System.out.println("2. Créer un trajet interurbain");
			System.out.println("3. Créer un trajet urbain");
			System.out.println("4. Gérer les réservations interurbaines");
			System.out.println("5. Gérer les colis interurbains");
			System.out.println("6. Quitter");
			System.out.print("Sélectionnez une option : ");

			String choice = scanner.nextLine();
			switch (choice) {
				case "1":
					createVehicle(scanner);
					break;
				case "2":
					createInterurbainRoute(scanner);
					break;
				case "3":
					createUrbainRoute(scanner);
					break;
				case "4":
					manageInterurbainReservations(scanner);
					break;
				case "5":
					manageColisInterurbains(scanner); // Méthode modifiée
					break;
				case "6":
					System.out.println("Fermeture de l'application.");
					scanner.close();
					return;
				default:
					System.out.println("Option invalide. Veuillez réessayer.");
			}
		}
	}

	/**
	 * Mise à jour de la capacité de volume et de poids des véhicules interurbains existants.
	 */
	private void updateVehiculeCapaciteVolume() {
		List<VehiculeInterurbain> vehicules = vehiculeService.getInterurbainVehicles();
		for (VehiculeInterurbain vehicule : vehicules) {
			// Définir la capacité de volume et de poids à 1000
			vehicule.setCapaciteVolume(1000.0); // En cm³
			vehicule.setCapacitePoids(1000.0);  // En kg
			vehiculeService.saveVehicule(vehicule);
		}
		System.out.println("Capacités des véhicules interurbains mises à jour.");
	}

	/**
	 * Création d'un véhicule urbain ou interurbain.
	 */
	private void createVehicle(Scanner scanner) {
		System.out.print("Type de véhicule (URBAIN/INTERURBAIN) : ");
		String type = scanner.nextLine().toUpperCase();

		System.out.print("Immatriculation : ");
		String immatriculation = scanner.nextLine();

		System.out.print("Marque : ");
		String marque = scanner.nextLine();

		System.out.print("Modèle : ");
		String modele = scanner.nextLine();

		if (type.equals("URBAIN")) {
			System.out.print("Adresse MAC : ");
			String macAddress = scanner.nextLine();

			VehiculeUrbain urbainVehicule = new VehiculeUrbain();
			urbainVehicule.setImmatriculation(immatriculation);
			urbainVehicule.setMarque(marque);
			urbainVehicule.setModele(modele);
			urbainVehicule.setMacAddress(macAddress);
			urbainVehicule.setTypeVehicule("URBAIN");

			vehiculeService.saveVehicule(urbainVehicule);
			System.out.println("Véhicule urbain créé avec succès : " + urbainVehicule);

		} else if (type.equals("INTERURBAIN")) {
			System.out.print("Capacité de passagers : ");
			int capacite;
			try {
				capacite = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Capacité invalide. Opération annulée.");
				return;
			}

			// Définir la capacité de volume et de poids par défaut à 1000
			double capaciteVolume = 1000.0; // 1000 cm³ par défaut
			double capacitePoids = 1000.0;  // 1000 kg par défaut

			System.out.println("Capacité de volume par défaut : " + capaciteVolume + " cm³");
			System.out.println("Capacité de poids par défaut : " + capacitePoids + " kg");

			VehiculeInterurbain interurbainVehicule = new VehiculeInterurbain();
			interurbainVehicule.setImmatriculation(immatriculation);
			interurbainVehicule.setMarque(marque);
			interurbainVehicule.setModele(modele);
			interurbainVehicule.setCapacite(capacite);
			interurbainVehicule.setCapaciteVolume(capaciteVolume);
			interurbainVehicule.setCapacitePoids(capacitePoids);
			interurbainVehicule.setTypeVehicule("INTERURBAIN");

			// Sauvegarder le véhicule et récupérer l'instance persistée avec l'ID généré
			VehiculeInterurbain savedVehicule = (VehiculeInterurbain) vehiculeService.saveVehicule(interurbainVehicule);
			System.out.println("Véhicule interurbain créé avec succès : " + savedVehicule);

			// Générer les places pour le véhicule interurbain
			seatService.generateSeatsForVehicule(savedVehicule);
			System.out.println("Places générées pour le véhicule.");
		} else {
			System.out.println("Type de véhicule invalide.");
		}
	}

	/**
	 * Création d'un trajet interurbain.
	 */
	private void createInterurbainRoute(Scanner scanner) {
		List<VehiculeInterurbain> interurbainVehicules = vehiculeService.getInterurbainVehicles();
		if (interurbainVehicules.isEmpty()) {
			System.out.println("Aucun véhicule interurbain disponible.");
			return;
		}

		System.out.println("Sélectionnez l'ID du véhicule interurbain pour le trajet :");
		interurbainVehicules.forEach(v -> System.out.println("ID: " + v.getId() + " - " + v.getMarque() + " " + v.getModele()));

		Long vehiculeId;
		try {
			vehiculeId = Long.parseLong(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("ID invalide. Opération annulée.");
			return;
		}
		VehiculeInterurbain vehicule = (VehiculeInterurbain) vehiculeService.getVehiculeById(vehiculeId);

		if (vehicule != null) {
			System.out.print("Nom de la ligne : ");
			String nomLigne = scanner.nextLine();

			System.out.print("Lieu de départ : ");
			String lieuDepart = scanner.nextLine();

			System.out.print("Lieu d'arrivée : ");
			String lieuArrivee = scanner.nextLine();

			String ville = selectCity(scanner);
			if (ville == null || ville.isEmpty()) {
				System.out.println("La ville ne peut pas être nulle.");
				return;
			}

			System.out.print("Montant : ");
			double montant;
			try {
				montant = Double.parseDouble(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Montant invalide. Opération annulée.");
				return;
			}

			// Demander la date de départ
			System.out.print("Date de départ (format YYYY-MM-DD) : ");
			LocalDate dateDepart;
			try {
				dateDepart = LocalDate.parse(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Format de la date de départ invalide. Opération annulée.");
				return;
			}

			System.out.print("Heure de départ (format HH:mm) : ");
			LocalTime heureDepartTime;
			try {
				heureDepartTime = LocalTime.parse(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Format de l'heure de départ invalide. Opération annulée.");
				return;
			}
			LocalDateTime heureDepart = LocalDateTime.of(dateDepart, heureDepartTime);

			// Demander la date d'arrivée
			System.out.print("Date d'arrivée (format YYYY-MM-DD) : ");
			LocalDate dateArrivee;
			try {
				dateArrivee = LocalDate.parse(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Format de la date d'arrivée invalide. Opération annulée.");
				return;
			}

			System.out.print("Heure d'arrivée (format HH:mm) : ");
			LocalTime heureArriveeTime;
			try {
				heureArriveeTime = LocalTime.parse(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Format de l'heure d'arrivée invalide. Opération annulée.");
				return;
			}
			LocalDateTime heureArrivee = LocalDateTime.of(dateArrivee, heureArriveeTime);

			LigneTrajetInterurbain trajet = new LigneTrajetInterurbain();
			trajet.setNomLigne(nomLigne);
			trajet.setLieuDepart(lieuDepart);
			trajet.setLieuArrivee(lieuArrivee);
			trajet.setVille(ville);
			trajet.setMontant(montant);
			trajet.setHeureDepart(heureDepart);
			trajet.setHeureArrivee(heureArrivee);
			trajet.setTypeLigne("INTERURBAIN");
			trajet.setVehicule(vehicule);

			try {
				ligneTrajetService.saveLigne(trajet);
				System.out.println("Trajet interurbain créé avec succès : " + trajet);
			} catch (Exception e) {
				System.out.println("Erreur lors de la création du trajet : " + e.getMessage());
			}
		} else {
			System.out.println("ID de véhicule non valide ou non interurbain.");
		}
	}

	/**
	 * Création d'un trajet urbain.
	 */
	private void createUrbainRoute(Scanner scanner) {
		List<VehiculeUrbain> urbainVehicules = vehiculeService.getUrbainVehicles();
		if (urbainVehicules.isEmpty()) {
			System.out.println("Aucun véhicule urbain disponible.");
			return;
		}

		System.out.println("Sélectionnez l'ID du véhicule urbain pour le trajet :");
		urbainVehicules.forEach(v -> System.out.println("ID: " + v.getId() + " - " + v.getMarque() + " " + v.getModele()));

		Long vehiculeId;
		try {
			vehiculeId = Long.parseLong(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("ID invalide. Opération annulée.");
			return;
		}
		VehiculeUrbain vehicule = (VehiculeUrbain) vehiculeService.getVehiculeById(vehiculeId);

		if (vehicule != null) {
			System.out.print("Nom de la ligne : ");
			String nomLigne = scanner.nextLine();

			String ville = selectCity(scanner);
			if (ville == null || ville.isEmpty()) {
				System.out.println("La ville ne peut pas être nulle.");
				return;
			}

			LigneTrajetUrbain trajet = new LigneTrajetUrbain();
			trajet.setNomLigne(nomLigne);
			trajet.setTypeLigne("URBAIN");
			trajet.setVille(ville);
			trajet.setVehicule(vehicule);

			try {
				ligneTrajetService.saveLigne(trajet);
				System.out.println("Trajet urbain créé avec succès : " + trajet);
			} catch (Exception e) {
				System.out.println("Erreur lors de la création du trajet : " + e.getMessage());
			}
		} else {
			System.out.println("ID de véhicule non valide ou non urbain.");
		}
	}

	/**
	 * Gestion des réservations interurbaines.
	 */
	private void manageInterurbainReservations(Scanner scanner) {
		System.out.println("\nGestion des Réservations Interurbaines");

		// Étape 1 : Afficher les dates disponibles
		List<LocalDate> datesDisponibles = ligneTrajetService.getAvailableDates();
		if (datesDisponibles.isEmpty()) {
			System.out.println("Aucune date disponible pour les trajets interurbains.");
			return;
		}

		System.out.println("Dates disponibles :");
		for (int i = 0; i < datesDisponibles.size(); i++) {
			System.out.println((i + 1) + ". " + datesDisponibles.get(i));
		}

		System.out.print("Sélectionnez le numéro de la date : ");
		int dateChoice;
		try {
			dateChoice = Integer.parseInt(scanner.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		if (dateChoice < 0 || dateChoice >= datesDisponibles.size()) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		LocalDate selectedDate = datesDisponibles.get(dateChoice);

		// Étape 2 : Afficher les destinations disponibles pour la date sélectionnée
		List<String> destinationsDisponibles = ligneTrajetService.getAvailableDestinationsByDate(selectedDate);
		if (destinationsDisponibles.isEmpty()) {
			System.out.println("Aucune destination disponible pour cette date.");
			return;
		}

		System.out.println("Destinations disponibles pour le " + selectedDate + " :");
		for (int i = 0; i < destinationsDisponibles.size(); i++) {
			System.out.println((i + 1) + ". " + destinationsDisponibles.get(i));
		}

		System.out.print("Sélectionnez le numéro de la destination : ");
		int destinationChoice;
		try {
			destinationChoice = Integer.parseInt(scanner.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		if (destinationChoice < 0 || destinationChoice >= destinationsDisponibles.size()) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		String selectedDestination = destinationsDisponibles.get(destinationChoice);

		// Étape 3 : Afficher les voyages disponibles pour la date et la destination sélectionnées
		List<LigneTrajetInterurbain> voyagesDisponibles = ligneTrajetService.getInterurbainLignesByDateAndDestination(selectedDate, selectedDestination);
		if (voyagesDisponibles.isEmpty()) {
			System.out.println("Aucun voyage disponible pour cette date et destination.");
			return;
		}

		System.out.println("Voyages disponibles :");
		for (int i = 0; i < voyagesDisponibles.size(); i++) {
			LigneTrajetInterurbain voyage = voyagesDisponibles.get(i);
			System.out.println((i + 1) + ". ID: " + voyage.getId() + " - " + voyage.getNomLigne() + " - Départ: " + voyage.getHeureDepart().toLocalTime());
		}

		System.out.print("Sélectionnez le numéro du voyage : ");
		int voyageChoice;
		try {
			voyageChoice = Integer.parseInt(scanner.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		if (voyageChoice < 0 || voyageChoice >= voyagesDisponibles.size()) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		LigneTrajetInterurbain voyage = voyagesDisponibles.get(voyageChoice);

		// Afficher le statut des places
		displaySeatStatus(voyage);

		// Sélection et réservation des places
		// Récupérer les places disponibles
		List<Seat> seats = seatService.getSeatsByVehicule(voyage.getVehicule());
		List<Reservation> reservations = reservationService.getReservationsByTrajet(voyage);

		Set<Long> reservedSeatIds = reservations.stream()
				.map(r -> r.getSeat().getId())
				.collect(Collectors.toSet());

		List<Seat> availableSeats = seats.stream()
				.filter(seat -> !reservedSeatIds.contains(seat.getId()))
				.collect(Collectors.toList());

		if (availableSeats.isEmpty()) {
			System.out.println("Aucune place disponible pour ce voyage.");
			return;
		}

		System.out.println("Places disponibles :");
		for (int i = 0; i < availableSeats.size(); i++) {
			Seat seat = availableSeats.get(i);
			System.out.println((i + 1) + ". Place numéro " + seat.getSeatNumber());
		}

		System.out.print("Sélectionnez le numéro de la place à réserver : ");
		int seatChoice;
		try {
			seatChoice = Integer.parseInt(scanner.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		if (seatChoice < 0 || seatChoice >= availableSeats.size()) {
			System.out.println("Choix invalide. Opération annulée.");
			return;
		}

		Seat selectedSeat = availableSeats.get(seatChoice);

		// Saisie des informations du passager
		Passager passager = getPassengerInfo(scanner);

		// Demander la date de la réservation
		LocalDateTime reservationDate = getReservationDate(scanner);
		if (reservationDate == null) {
			System.out.println("Date de réservation invalide. Opération annulée.");
			return;
		}

		// Créer la réservation
		Reservation reservation = new Reservation();
		reservation.setReservationDate(reservationDate);
		reservation.setSeat(selectedSeat);
		reservation.setTrajet(voyage);
		reservation.setPassager(passager);

		try {
			reservationService.createReservation(reservation);
			System.out.println("Réservation créée avec succès.");

			// Enregistrement du colis
			System.out.print("Souhaitez-vous enregistrer un colis ? (O/N) : ");
			String enregistrerColis = scanner.nextLine().trim().toUpperCase();

			if ("O".equals(enregistrerColis)) {
				Colis colis = inputColisInformation(scanner, voyage, passager);
				if (colis != null) {
					generateColisLabel(colis);
					generateInvoice(colis);
					System.out.println("Colis enregistré avec succès. Étiquette et facture générées.");
				} else {
					System.out.println("Échec de l'enregistrement du colis.");
				}
			}

			// Afficher le billet de réservation
			System.out.println("Voici votre billet :");
			printReservationTicket(reservation);

		} catch (Exception e) {
			System.out.println("Erreur lors de la création de la réservation : " + e.getMessage());
		}
	}

	/**
	 * Gestion des colis interurbains.
	 * Permet de sélectionner un passager existant lors de la création d'un colis.
	 */
	private void manageColisInterurbains(Scanner scanner) {
		System.out.println("\nGestion des Colis Interurbains");

		// Étape 1 : Sélection du voyage
		LigneTrajetInterurbain voyage = selectVoyageForColis(scanner);
		if (voyage == null) {
			System.out.println("Aucun voyage sélectionné. Opération annulée.");
			return;
		}

		// Étape 2 : Afficher la disponibilité de l'espace de fret
		displayFreightAvailability(voyage);

		// Étape 3 : Sélection du passager existant
		Passager passager = selectPassengerForColis(scanner, voyage);

		if (passager == null) {
			System.out.println("Aucun passager sélectionné. Opération annulée.");
			return;
		}

		// Étape 4 : Saisie des informations du colis
		Colis colis = inputColisInformation(scanner, voyage, passager);

		if (colis == null) {
			System.out.println("Informations du colis invalides. Opération annulée.");
			return;
		}

		// Génération de l'étiquette et de la facture
		generateColisLabel(colis);
		generateInvoice(colis);

		System.out.println("Colis enregistré avec succès. Étiquette et facture générées.");
	}

	/**
	 * Sélectionne un voyage pour l'expédition du colis.
	 */
	private LigneTrajetInterurbain selectVoyageForColis(Scanner scanner) {
		System.out.println("Sélectionnez un voyage pour l'expédition du colis :");
		List<LigneTrajetInterurbain> voyagesDisponibles = ligneTrajetService.getAllInterurbainLignes();

		if (voyagesDisponibles.isEmpty()) {
			System.out.println("Aucun voyage disponible.");
			return null;
		}

		for (int i = 0; i < voyagesDisponibles.size(); i++) {
			LigneTrajetInterurbain voyage = voyagesDisponibles.get(i);
			System.out.println((i + 1) + ". ID: " + voyage.getId() + " - " + voyage.getNomLigne() + " - Départ: " + voyage.getHeureDepart());
		}

		System.out.print("Sélectionnez le numéro du voyage : ");
		int voyageChoice;
		try {
			voyageChoice = Integer.parseInt(scanner.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Choix invalide.");
			return null;
		}

		if (voyageChoice < 0 || voyageChoice >= voyagesDisponibles.size()) {
			System.out.println("Choix invalide.");
			return null;
		}

		return voyagesDisponibles.get(voyageChoice);
	}

	/**
	 * Affiche la disponibilité de l'espace de fret pour le voyage sélectionné.
	 */
	private void displayFreightAvailability(LigneTrajetInterurbain voyage) {
		VehiculeInterurbain vehicule = voyage.getVehicule();

		double capaciteVolumeMax = vehicule.getCapaciteVolume(); // En cm³
		double capacitePoidsMax = vehicule.getCapacitePoids();   // En kg

		// Calculer le volume et le poids déjà utilisés par les colis enregistrés pour ce voyage
		List<Colis> colisEnregistres = colisService.getColisByTrajet(voyage);

		double volumeUtilise = colisEnregistres.stream().mapToDouble(Colis::getVolume).sum();
		double poidsUtilise = colisEnregistres.stream().mapToDouble(Colis::getPoids).sum();

		double volumeRestant = capaciteVolumeMax - volumeUtilise;
		double poidsRestant = capacitePoidsMax - poidsUtilise;

		System.out.println("Espace de fret disponible :");
		System.out.println("Volume restant : " + volumeRestant + " cm³");
		System.out.println("Poids restant : " + poidsRestant + " kg");
	}

	/**
	 * Permet à l'utilisateur de sélectionner un passager existant pour le colis.
	 */
	private Passager selectPassengerForColis(Scanner scanner, LigneTrajetInterurbain voyage) {
		System.out.println("Sélectionnez le passager à qui appartient le colis :");

		// Récupérer les réservations pour le voyage
		List<Reservation> reservations = reservationService.getReservationsByTrajet(voyage);

		if (reservations.isEmpty()) {
			System.out.println("Aucun passager n'a réservé pour ce voyage.");
			return null;
		}

		// Extraire la liste des passagers
		List<Passager> passagers = reservations.stream()
				.map(Reservation::getPassager)
				.distinct()
				.collect(Collectors.toList());

		// Afficher la liste des passagers
		for (int i = 0; i < passagers.size(); i++) {
			Passager passager = passagers.get(i);
			System.out.println((i + 1) + ". " + passager.getNom() + " " + passager.getPrenom() + " (ID: " + passager.getId() + ")");
		}

		System.out.print("Sélectionnez le numéro du passager : ");
		int passagerChoice;
		try {
			passagerChoice = Integer.parseInt(scanner.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Choix invalide.");
			return null;
		}

		if (passagerChoice < 0 || passagerChoice >= passagers.size()) {
			System.out.println("Choix invalide.");
			return null;
		}

		return passagers.get(passagerChoice);
	}

	/**
	 * Saisie des informations du colis.
	 */
	private Colis inputColisInformation(Scanner scanner, LigneTrajetInterurbain voyage, Passager passager) {
		try {
			System.out.println("\nSaisie des informations du colis :");

			System.out.print("Longueur (en cm) : ");
			double longueur = Double.parseDouble(scanner.nextLine());

			System.out.print("Largeur (en cm) : ");
			double largeur = Double.parseDouble(scanner.nextLine());

			System.out.print("Hauteur (en cm) : ");
			double hauteur = Double.parseDouble(scanner.nextLine());

			System.out.print("Poids (en kg) : ");
			double poids = Double.parseDouble(scanner.nextLine());

			// Calcul du volume
			double volume = longueur * largeur * hauteur;

			// Vérifier si le colis peut être accepté en fonction de l'espace disponible
			VehiculeInterurbain vehicule = voyage.getVehicule();

			double capaciteVolumeMax = vehicule.getCapaciteVolume();
			double capacitePoidsMax = vehicule.getCapacitePoids();

			List<Colis> colisEnregistres = colisService.getColisByTrajet(voyage);

			double volumeUtilise = colisEnregistres.stream().mapToDouble(Colis::getVolume).sum();
			double poidsUtilise = colisEnregistres.stream().mapToDouble(Colis::getPoids).sum();

			if ((volumeUtilise + volume) > capaciteVolumeMax) {
				System.out.println("Impossible d'enregistrer ce colis : volume insuffisant.");
				return null;
			}

			if ((poidsUtilise + poids) > capacitePoidsMax) {
				System.out.println("Impossible d'enregistrer ce colis : poids insuffisant.");
				return null;
			}

			// Générer un code de suivi unique
			String codeSuivi = UUID.randomUUID().toString();

			Colis colis = new Colis();
			colis.setLongueur(longueur);
			colis.setLargeur(largeur);
			colis.setHauteur(hauteur);
			colis.setPoids(poids);
			colis.calculerVolume();
			colis.setCodeSuivi(codeSuivi);
			colis.setTrajet(voyage);
			colis.setDateEnvoi(LocalDateTime.now());
			colis.setPassager(passager);
			colis.setStatut(StatutColis.EN_ATTENTE_DEPART);

			// Enregistrer le colis
			colisService.saveColis(colis);

			return colis;

		} catch (NumberFormatException e) {
			System.out.println("Saisie invalide. Veuillez entrer des valeurs numériques.");
			return null;
		} catch (Exception e) {
			System.out.println("Erreur lors de la saisie des informations du colis : " + e.getMessage());
			return null;
		}
	}

	/**
	 * Génération de la facture pour le colis.
	 */
	private void generateInvoice(Colis colis) {
		// Chemin où le PDF sera enregistré
		String filePath = "invoices/invoice_" + colis.getCodeSuivi() + ".pdf";

		// Assurez-vous que le dossier 'invoices' existe ou créez-le
		File directory = new File("invoices");
		if (!directory.exists()) {
			directory.mkdir();
		}

		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			// Ajouter le contenu de la facture
			document.add(new Paragraph("Facture d'Expédition", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
			document.add(new Paragraph(" ")); // Ligne vide

			document.add(new Paragraph("Code de suivi : " + colis.getCodeSuivi()));
			document.add(new Paragraph("Date d'envoi : " + colis.getDateEnvoi().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

			// Informations du passager
			Passager passager = colis.getPassager();
			document.add(new Paragraph("Nom du client : " + passager.getNom() + " " + passager.getPrenom()));

			// Informations du voyage
			LigneTrajetInterurbain trajet = colis.getTrajet();
			document.add(new Paragraph("Voyage : " + trajet.getNomLigne()));
			document.add(new Paragraph("Départ : " + trajet.getLieuDepart() + " à " + trajet.getHeureDepart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
			document.add(new Paragraph("Arrivée : " + trajet.getLieuArrivee() + " à " + trajet.getHeureArrivee().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

			// Informations du colis
			document.add(new Paragraph("Dimensions : " + colis.getLongueur() + " x " + colis.getLargeur() + " x " + colis.getHauteur() + " cm"));
			document.add(new Paragraph("Poids : " + colis.getPoids() + " kg"));

			// Montant à payer
			double montant = calculerMontant(colis);
			document.add(new Paragraph("Montant à payer : " + String.format("%.2f", montant) + " EUR"));

			document.close();
		} catch (Exception e) {
			System.out.println("Erreur lors de la génération de la facture : " + e.getMessage());
		}
	}

	/**
	 * Calcul du montant à payer pour le colis.
	 */
	private double calculerMontant(Colis colis) {
		// Exemple simple : montant basé sur le poids et le volume
		double tarifParKg = 2.0; // Tarif par kg
		double tarifParCm3 = 0.0001; // Tarif par cm³

		double montantPoids = colis.getPoids() * tarifParKg;
		double montantVolume = colis.getVolume() * tarifParCm3;

		return montantPoids + montantVolume;
	}

	/**
	 * Génération de l'étiquette du colis.
	 */
	private void generateColisLabel(Colis colis) {
		// Chemin où le PDF sera enregistré
		String filePath = "labels/label_" + colis.getCodeSuivi() + ".pdf";

		// Assurez-vous que le dossier 'labels' existe ou créez-le
		File directory = new File("labels");
		if (!directory.exists()) {
			directory.mkdir();
		}

		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			// Ajouter le contenu de l'étiquette
			document.add(new Paragraph("Étiquette du Colis", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
			document.add(new Paragraph(" ")); // Ligne vide

			document.add(new Paragraph("Code de suivi : " + colis.getCodeSuivi()));
			document.add(new Paragraph("Date d'envoi : " + colis.getDateEnvoi().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

			// Informations du voyage
			LigneTrajetInterurbain trajet = colis.getTrajet();
			document.add(new Paragraph("Départ : " + trajet.getLieuDepart()));
			document.add(new Paragraph("Arrivée : " + trajet.getLieuArrivee()));

			// Informations du colis
			document.add(new Paragraph("Dimensions : " + colis.getLongueur() + " x " + colis.getLargeur() + " x " + colis.getHauteur() + " cm"));
			document.add(new Paragraph("Poids : " + colis.getPoids() + " kg"));

			// Génération d'un QR code (simulation)
			document.add(new Paragraph("QR Code : [QR CODE SIMULÉ]"));

			document.close();
		} catch (Exception e) {
			System.out.println("Erreur lors de la génération de l'étiquette : " + e.getMessage());
		}
	}

	/**
	 * Affiche le statut des places pour un voyage donné.
	 */
	private void displaySeatStatus(LigneTrajetInterurbain voyage) {
		System.out.println("\nStatut des places pour le voyage sélectionné :");

		List<Seat> seats = seatService.getSeatsByVehicule(voyage.getVehicule());
		List<Reservation> reservations = reservationService.getReservationsByTrajet(voyage);

		Set<Long> reservedSeatIds = reservations.stream()
				.map(r -> r.getSeat().getId())
				.collect(Collectors.toSet());

		for (Seat seat : seats) {
			String status = reservedSeatIds.contains(seat.getId()) ? "Réservée" : "Disponible";
			String colorCode = reservedSeatIds.contains(seat.getId()) ? "\u001B[31m" : "\u001B[32m"; // Rouge ou Vert
			System.out.print(colorCode + "[" + seat.getSeatNumber() + " - " + status + "] " + "\u001B[0m");
			if (seat.getSeatNumber() % 4 == 0) {
				System.out.println(); // Nouvelle ligne toutes les 4 places pour la mise en forme
			}
		}
		System.out.println();
	}

	/**
	 * Obtient les informations du passager, soit en le sélectionnant par sa carte client, soit en créant un nouveau.
	 */
	private Passager getPassengerInfo(Scanner scanner) {
		System.out.print("Le client possède-t-il une carte client ? (O/N) : ");
		String hasCard = scanner.nextLine().trim().toUpperCase();

		Passager passager;

		if ("O".equals(hasCard)) {
			System.out.print("Entrez l'identifiant de la carte client : ");
			String carteClient = scanner.nextLine();
			passager = passagerService.getPassagerByCarteClient(carteClient);
			if (passager == null) {
				System.out.println("Aucun passager trouvé avec cette carte client. Saisie des informations du passager.");
				passager = createNewPassenger(scanner, carteClient);
			}
		} else {
			passager = createNewPassenger(scanner, null);
		}

		return passager;
	}

	/**
	 * Crée un nouveau passager avec les informations fournies.
	 */
	private Passager createNewPassenger(Scanner scanner, String carteClient) {
		System.out.print("Nom : ");
		String nom = scanner.nextLine();

		System.out.print("Prénom : ");
		String prenom = scanner.nextLine();

		System.out.print("Date de naissance (format YYYY-MM-DD) : ");
		String dateNaissance = scanner.nextLine();

		Passager passager = new Passager();
		passager.setNom(nom);
		passager.setPrenom(prenom);
		passager.setDateNaissance(dateNaissance);
		passager.setCarteClient(carteClient);

		return passagerService.savePassager(passager);
	}

	/**
	 * Obtient la date de réservation auprès de l'utilisateur.
	 */
	private LocalDateTime getReservationDate(Scanner scanner) {
		System.out.print("Entrez la date de la réservation (format YYYY-MM-DD HH:mm) : ");
		String dateInput = scanner.nextLine();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		try {
			return LocalDateTime.parse(dateInput, formatter);
		} catch (DateTimeParseException e) {
			System.out.println("Format de date invalide. Veuillez utiliser le format YYYY-MM-DD HH:mm.");
			return null;
		}
	}

	/**
	 * Affiche le billet de réservation.
	 */
	private void printReservationTicket(Reservation reservation) {
		System.out.println("\n=== Billet de Transport ===");
		System.out.println("Numéro de réservation : " + reservation.getId());
		System.out.println("Date de réservation : " + reservation.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		System.out.println("\n--- Informations du Passager ---");
		Passager passager = reservation.getPassager();
		System.out.println("Nom : " + passager.getNom());
		System.out.println("Prénom : " + passager.getPrenom());
		System.out.println("Date de naissance : " + passager.getDateNaissance());
		if (passager.getCarteClient() != null) {
			System.out.println("Carte Client : " + passager.getCarteClient());
		}

		System.out.println("\n--- Détails du Trajet ---");
		LigneTrajetInterurbain trajet = (LigneTrajetInterurbain) reservation.getTrajet();
		System.out.println("Nom de la ligne : " + trajet.getNomLigne());
		System.out.println("Lieu de départ : " + trajet.getLieuDepart());
		System.out.println("Lieu d'arrivée : " + trajet.getLieuArrivee());
		System.out.println("Date et heure de départ : " + trajet.getHeureDepart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		System.out.println("Date et heure d'arrivée : " + trajet.getHeureArrivee().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		System.out.println("Montant : " + trajet.getMontant() + " EUR");

		System.out.println("\n--- Détails du Véhicule ---");
		VehiculeInterurbain vehicule = (VehiculeInterurbain) trajet.getVehicule();
		System.out.println("Immatriculation : " + vehicule.getImmatriculation());
		System.out.println("Marque : " + vehicule.getMarque());
		System.out.println("Modèle : " + vehicule.getModele());

		System.out.println("\n--- Détails de la Place ---");
		Seat seat = reservation.getSeat();
		System.out.println("Numéro de place : " + seat.getSeatNumber());

		System.out.println("\nMerci d'avoir réservé avec nous ! Bon voyage.");
		System.out.println("==============================\n");
	}

	/**
	 * Permet à l'utilisateur de sélectionner une ville parmi une liste prédéfinie.
	 */
	private String selectCity(Scanner scanner) {
		String[] villes = {"Libreville", "Port-Gentil", "Franceville", "Oyem", "Moanda", "Mouila", "Lambaréné", "Tchibanga", "Koulamoutou"};

		System.out.println("Sélectionnez la ville parmi les options suivantes :");
		for (int i = 0; i < villes.length; i++) {
			System.out.println((i + 1) + ". " + villes[i]);
		}

		int villeIndex = -1;
		while (villeIndex < 1 || villeIndex > villes.length) {
			System.out.print("Choisissez une option (1-" + villes.length + ") : ");
			try {
				villeIndex = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer un numéro valide.");
				continue;
			}
			if (villeIndex < 1 || villeIndex > villes.length) {
				System.out.println("Option invalide. Veuillez choisir un nombre entre 1 et " + villes.length + ".");
			}
		}
		return villes[villeIndex - 1];
	}

	/**
	 * Configuration du CORS pour l'application.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOriginPatterns("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}
