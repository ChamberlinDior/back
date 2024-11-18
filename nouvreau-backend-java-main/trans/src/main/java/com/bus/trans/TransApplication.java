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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
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

	public static void main(String[] args) {
		SpringApplication.run(TransApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Backend is running...");
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\nOptions disponibles : ");
			System.out.println("1. Créer un véhicule");
			System.out.println("2. Créer un trajet interurbain");
			System.out.println("3. Créer un trajet urbain");
			System.out.println("4. Gérer les réservations interurbaines");
			System.out.println("5. Quitter");
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
					System.out.println("Fermeture de l'application.");
					return;
				default:
					System.out.println("Option invalide. Veuillez réessayer.");
			}
		}
	}

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
			System.out.print("Capacité : ");
			int capacite;
			try {
				capacite = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Capacité invalide. Opération annulée.");
				return;
			}

			VehiculeInterurbain interurbainVehicule = new VehiculeInterurbain();
			interurbainVehicule.setImmatriculation(immatriculation);
			interurbainVehicule.setMarque(marque);
			interurbainVehicule.setModele(modele);
			interurbainVehicule.setCapacite(capacite);
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
			System.out.println("Réservation créée avec succès. Voici votre billet :");
			printReservationTicket(reservation); // Afficher les détails de la réservation
		} catch (Exception e) {
			System.out.println("Erreur lors de la création de la réservation : " + e.getMessage());
		}
	}

	// Nouvelle méthode pour afficher les détails de la réservation comme un billet
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

	private Passager getPassengerInfo(Scanner scanner) {
		System.out.print("Le client possède-t-il une carte client ? (O/N) : ");
		String hasCard = scanner.nextLine().trim().toUpperCase();

		Passager passager;

		if ("O".equals(hasCard)) {
			System.out.print("Entrez l'identifiant de la carte client : ");
			String carteClient = scanner.nextLine();
			passager = reservationService.getPassagerByCarteClient(carteClient);
			if (passager == null) {
				System.out.println("Aucun passager trouvé avec cette carte client. Saisie des informations du passager.");
				passager = createNewPassenger(scanner, carteClient);
			}
		} else {
			passager = createNewPassenger(scanner, null);
		}

		return passager;
	}

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

		return reservationService.savePassager(passager);
	}

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
