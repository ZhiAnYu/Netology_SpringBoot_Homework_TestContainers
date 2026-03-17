package ru.netology.SpringBootDemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootDemoApplicationTests {
    @Autowired
    TestRestTemplate testRestTemplate;


    @Container
    private final GenericContainer<?> devapp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);
    @Container
    private final GenericContainer<?> prodapp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);


    @Test
    void testDevProfile() {
        Integer port = devapp.getMappedPort(8080);
        String url = "http://localhost:" + port + "/profile";
        ResponseEntity<String> forEntity = testRestTemplate.getForEntity(url, String.class);
        // Проверка статуса
        assertEquals(200, forEntity.getStatusCodeValue(), "Dev приложение должно вернуть 200 OK");

        // Проверка содержимого
        assertTrue(forEntity.getBody().contains("Current profile is dev"),
                "Ответ Dev приложения должен быть 'Current profile is dev'. Получено: " + forEntity.getBody());

        System.out.println("Dev Test Passed. Response: " + forEntity.getBody());
    }

    @Test
    void testProdProfile() {
        Integer port = prodapp.getMappedPort(8081);
        String url = "http://localhost:" + port + "/profile";
        ResponseEntity<String> forEntity = testRestTemplate.getForEntity(url, String.class);
        // Проверка статуса
        assertEquals(200, forEntity.getStatusCodeValue(),
                "Prod приложение должно вернуть 200 OK");

        // Проверка содержимого
        assertTrue(forEntity.getBody().contains("Current profile is production"),
                "Ответ Prod приложения должен быть 'Current profile is production'. Получено: "
                        + forEntity.getBody());

        System.out.println("Prod Test Passed. Response: " + forEntity.getBody());
    }
}
