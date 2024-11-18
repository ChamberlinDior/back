package com.bus.trans;

import com.bus.trans.model.VehiculeInterurbain;
import com.bus.trans.model.VehiculeUrbain;
import com.bus.trans.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EntityScan(basePackages = "com.bus.trans.model")
@SpringBootApplication
public class TransApplication implements CommandLineRunner {

	@Autowired
	private VehiculeService vehiculeService;

	public static void main(String[] args) {
		SpringApplication.run(TransApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Backend is running...");

		// Simulation de création de véhicule urbain
		VehiculeUrbain vehiculeUrbain = new VehiculeUrbain();
		vehiculeUrbain.setImmatriculation("URB12345");
		vehiculeUrbain.setMarque("Toyota");
		vehiculeUrbain.setModele("Hiace");
		vehiculeUrbain.setTrajet("Libreville-Centre");
		vehiculeUrbain.setChauffeur("Jean Dupont");
		vehiculeUrbain.setMacAddress("00:1A:2B:3C:4D:5E");
		vehiculeUrbain.setTypeVehicule("URBAIN");

		VehiculeUrbain savedUrbain = (VehiculeUrbain) vehiculeService.saveVehicule(vehiculeUrbain);
		System.out.println("Véhicule urbain créé: " + savedUrbain);

		// Simulation de création de véhicule interurbain
		VehiculeInterurbain vehiculeInterurbain = new VehiculeInterurbain();
		vehiculeInterurbain.setImmatriculation("INTER67890");
		vehiculeInterurbain.setMarque("Mercedes");
		vehiculeInterurbain.setModele("Sprinter");
		vehiculeInterurbain.setTrajet("Libreville-Port-Gentil");
		vehiculeInterurbain.setChauffeur("Alice Koumba");
		vehiculeInterurbain.setCapacite(30);
		vehiculeInterurbain.setTypeVehicule("INTERURBAIN");

		VehiculeInterurbain savedInterurbain = (VehiculeInterurbain) vehiculeService.saveVehicule(vehiculeInterurbain);
		System.out.println("Véhicule interurbain créé: " + savedInterurbain);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOriginPatterns("*")  // Autoriser toutes les origines
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*")
						.allowCredentials(true); // Credentials allowed
			}
		};
	}
}
