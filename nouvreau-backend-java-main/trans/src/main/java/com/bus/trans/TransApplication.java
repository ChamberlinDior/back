package com.bus.trans;

import com.bus.trans.model.LigneTrajetInterurbain;
import com.bus.trans.model.LigneTrajetUrbain;
import com.bus.trans.model.Vehicule;
import com.bus.trans.model.VehiculeInterurbain;
import com.bus.trans.model.VehiculeUrbain;
import com.bus.trans.service.LigneTrajetService;
import com.bus.trans.service.VehiculeService;
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
import java.util.List;
import java.util.Scanner;

@EntityScan(basePackages = "com.bus.trans.model")
@SpringBootApplication
public class TransApplication implements CommandLineRunner {

	@Autowired
	private VehiculeService vehiculeService;

	@Autowired
	private LigneTrajetService ligneTrajetService;

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
			System.out.println("4. Quitter");
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
			int capacite = Integer.parseInt(scanner.nextLine());

			VehiculeInterurbain interurbainVehicule = new VehiculeInterurbain();
			interurbainVehicule.setImmatriculation(immatriculation);
			interurbainVehicule.setMarque(marque);
			interurbainVehicule.setModele(modele);
			interurbainVehicule.setCapacite(capacite);
			interurbainVehicule.setTypeVehicule("INTERURBAIN");

			vehiculeService.saveVehicule(interurbainVehicule);
			System.out.println("Véhicule interurbain créé avec succès : " + interurbainVehicule);
		} else {
			System.out.println("Type de véhicule invalide.");
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

		Long vehiculeId = null;
		try {
			vehiculeId = Long.parseLong(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("ID invalide. Opération annulée.");
			return;
		}
		Vehicule vehicule = vehiculeService.getVehiculeById(vehiculeId);

		if (vehicule instanceof VehiculeUrbain) {
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
			trajet.setVehicule((VehiculeUrbain) vehicule);

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

	private void createInterurbainRoute(Scanner scanner) {
		List<VehiculeInterurbain> interurbainVehicules = vehiculeService.getInterurbainVehicles();
		if (interurbainVehicules.isEmpty()) {
			System.out.println("Aucun véhicule interurbain disponible.");
			return;
		}

		System.out.println("Sélectionnez l'ID du véhicule interurbain pour le trajet :");
		interurbainVehicules.forEach(v -> System.out.println("ID: " + v.getId() + " - " + v.getMarque() + " " + v.getModele()));

		Long vehiculeId = null;
		try {
			vehiculeId = Long.parseLong(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("ID invalide. Opération annulée.");
			return;
		}
		Vehicule vehicule = vehiculeService.getVehiculeById(vehiculeId);

		if (vehicule instanceof VehiculeInterurbain) {
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

			System.out.print("Heure de départ (format HH:mm) : ");
			LocalTime heureDepartTime;
			try {
				heureDepartTime = LocalTime.parse(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Format de l'heure de départ invalide. Opération annulée.");
				return;
			}
			LocalDateTime heureDepart = LocalDateTime.of(LocalDate.now(), heureDepartTime);

			System.out.print("Heure d'arrivée (format HH:mm) : ");
			LocalTime heureArriveeTime;
			try {
				heureArriveeTime = LocalTime.parse(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Format de l'heure d'arrivée invalide. Opération annulée.");
				return;
			}
			LocalDateTime heureArrivee = LocalDateTime.of(LocalDate.now(), heureArriveeTime);

			LigneTrajetInterurbain trajet = new LigneTrajetInterurbain();
			trajet.setNomLigne(nomLigne);
			trajet.setLieuDepart(lieuDepart);
			trajet.setLieuArrivee(lieuArrivee);
			trajet.setVille(ville);
			trajet.setMontant(montant);
			trajet.setHeureDepart(heureDepart);
			trajet.setHeureArrivee(heureArrivee);
			trajet.setTypeLigne("INTERURBAIN");
			trajet.setVehicule((VehiculeInterurbain) vehicule);

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

	private String selectCity(Scanner scanner) {
		String[] villes = {"Libreville", "Port-Gentil", "Franceville", "Oyem", "Moanda", "Mouila", "Lambaréné", "Tchibanga", "Koulamoutou"};

		System.out.println("Sélectionnez la ville parmi les options suivantes :");
		for (int i = 0; i < villes.length; i++) {
			System.out.println((i + 1) + ". " + villes[i]);
		}

		int villeIndex = -1;
		while (villeIndex < 1 || villeIndex > villes.length) {
			System.out.print("Choisissez une option (1-9) : ");
			try {
				villeIndex = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer un numéro valide.");
				continue;
			}
			if (villeIndex < 1 || villeIndex > villes.length) {
				System.out.println("Option invalide. Veuillez choisir un nombre entre 1 et 9.");
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
