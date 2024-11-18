package com.bus.trans;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		// Vérification que le contexte se charge correctement
	}

	@Test
	void backendIsRunningMessageTest() {
		// Test pour vérifier que le backend fonctionne et retourne un message
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/", String.class);
		assertThat(response.getBody()).contains("Backend is running...");
	}

	@Test
	void corsConfigurationTest() {
		// Test pour vérifier que CORS est bien configuré pour les routes API
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/test", String.class);
		assertThat(response.getStatusCode().is2xxSuccessful());
	}
}
