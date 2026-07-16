package ru.netology.springBoot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ApplicationTests {
	@Container
	private static final GenericContainer<?> devApp = new GenericContainer<>("devapp:latest")
			.withExposedPorts(8080);

	@Container
	private static final GenericContainer<?> prodApp = new GenericContainer<>("prodapp:latest")
			.withExposedPorts(8081);

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	public static void setUp() {
		devApp.start();
		prodApp.start();

		System.out.println("Dev app started on port: " + devApp.getMappedPort(8080));
		System.out.println("Prod app started on port: " + prodApp.getMappedPort(8081));
	}

	@Test
	void testDevProfile() {
		Integer devPort = devApp.getMappedPort(8080);
		String url = "http://localhost:" + devPort + "/profile";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String expected = "Current profile is dev";
		String actual = response.getBody();

		System.out.println("DEV response: " + actual);
		assertEquals(expected, actual, "DEV профиль должен возвращать правильное сообщение");
	}

	@Test
	void testProdProfile() {
		Integer prodPort = prodApp.getMappedPort(8081);
		String url = "http://localhost:" + prodPort + "/profile";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String expected = "Current profile is production";
		String actual = response.getBody();

		System.out.println("PROD response: " + actual);
		assertEquals(expected, actual, "PROD профиль должен возвращать правильное сообщение");
	}

	@Test
	void testBothProfilesAreDifferent() {
		Integer devPort = devApp.getMappedPort(8080);
		Integer prodPort = prodApp.getMappedPort(8081);

		String devResponse = restTemplate.getForEntity(
				"http://localhost:" + devPort + "/profile", String.class).getBody();
		String prodResponse = restTemplate.getForEntity(
				"http://localhost:" + prodPort + "/profile", String.class).getBody();

		System.out.println("DEV response: " + devResponse);
		System.out.println("PROD response: " + prodResponse);

		assertEquals("Current profile is dev", devResponse);
		assertEquals("Current profile is production", prodResponse);
	}

	@Test
	void contextLoads() {
		ResponseEntity<String> devEntity = restTemplate.getForEntity("http://localhost:" + devApp.getMappedPort(8080), String.class);
		ResponseEntity<String> prodEntity = restTemplate.getForEntity("http://localhost:" + prodApp.getMappedPort(8081), String.class);

		System.out.println(devEntity.getBody());
		System.out.println(prodEntity.getBody());
	}
}
