package com.qapipeline.api;

import com.qapipeline.repository.TaskRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskUpdateApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Garante isolamento entre os testes
        taskRepository.deleteAll();
    }

    private Long createTask() {

        String requestBody = """
                {
                    "title": "Tarefa original",
                    "description": "Descrição original",
                    "priority": "LOW"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks");

        assertEquals(201, response.statusCode());

        Long taskId = response.jsonPath().getLong("id");

        assertNotNull(taskId);

        return taskId;
    }

    private String generateText(int length) {
        return "A".repeat(length);
    }

    // TS-019
    @Test
    @DisplayName("TS-019 - Atualizar tarefa existente")
    void ts019_shouldUpdateExistingTask() {

        Long taskId = createTask();

        String requestBody = """
                {
                    "title": "Tarefa atualizada",
                    "description": "Descrição atualizada pelo RF-004",
                    "priority": "HIGH"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/tasks/" + taskId);

        assertEquals(200, response.statusCode());

        assertEquals(
                taskId,
                response.jsonPath().getLong("id")
        );

        assertEquals(
                "Tarefa atualizada",
                response.jsonPath().getString("title")
        );

        assertEquals(
                "Descrição atualizada pelo RF-004",
                response.jsonPath().getString("description")
        );

        assertEquals(
                "HIGH",
                response.jsonPath().getString("priority")
        );

        assertEquals(
                "TODO",
                response.jsonPath().getString("status")
        );

        assertNotNull(
                response.jsonPath().getString("createdAt")
        );

        assertNotNull(
                response.jsonPath().getString("updatedAt")
        );
    }

    // TS-020
    @Test
    @DisplayName("TS-020 - Atualizar tarefa utilizando título vazio")
    void ts020_shouldRejectUpdateWithEmptyTitle() {

        Long taskId = createTask();

        String requestBody = """
                {
                    "title": "",
                    "description": "Teste de atualização com título vazio",
                    "priority": "HIGH"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/tasks/" + taskId);

        assertEquals(400, response.statusCode());
    }

    // TS-021
    @Test
    @DisplayName("TS-021 - Atualizar tarefa com título acima de 100 caracteres")
    void ts021_shouldRejectUpdateWithTitleAboveMaximumLength() {

        Long taskId = createTask();

        String title = generateText(101);

        String requestBody = """
                {
                    "title": "%s",
                    "description": "Teste de título acima do limite",
                    "priority": "MEDIUM"
                }
                """.formatted(title);

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/tasks/" + taskId);

        assertEquals(400, response.statusCode());
    }

    // TS-022
    @Test
    @DisplayName("TS-022 - Atualizar tarefa utilizando ID inexistente")
    void ts022_shouldReturn404WhenUpdatingNonexistentTask() {

        Long nonexistentId = 999999L;

        String requestBody = """
                {
                    "title": "Tentativa de atualização",
                    "description": "Tarefa inexistente",
                    "priority": "MEDIUM"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/tasks/" + nonexistentId);

        assertEquals(404, response.statusCode());

        boolean exists = taskRepository.existsById(nonexistentId);

        assertEquals(false, exists);
    }
}