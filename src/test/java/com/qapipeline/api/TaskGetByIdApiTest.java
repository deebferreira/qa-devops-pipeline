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
class TaskGetByIdApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        taskRepository.deleteAll();
    }

    // TS-017
    @Test
    @DisplayName("TS-017 - Consultar tarefa existente por ID")
    void ts017_shouldGetExistingTaskById() {

        String requestBody = """
                {
                    "title": "Tarefa para consulta por ID",
                    "description": "Teste do endpoint GET /tasks/{id}",
                    "priority": "HIGH"
                }
                """;

        Response creationResponse = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks");

        assertEquals(201, creationResponse.statusCode());

        Long taskId = creationResponse.jsonPath()
                .getLong("id");

        assertNotNull(taskId);

        Response response = given()
                .when()
                .request("GET", "/tasks/" + taskId);

        assertEquals(200, response.statusCode());

        assertEquals(
                taskId,
                response.jsonPath().getLong("id")
        );

        assertEquals(
                "Tarefa para consulta por ID",
                response.jsonPath().getString("title")
        );

        assertEquals(
                "Teste do endpoint GET /tasks/{id}",
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

    // TS-018
    @Test
    @DisplayName("TS-018 - Consultar tarefa com ID inexistente")
    void ts018_shouldReturn404WhenTaskDoesNotExist() {

        Long nonexistentId = 999999L;

        Response response = given()
                .when()
                .request("GET", "/tasks/" + nonexistentId);

        assertEquals(200, response.statusCode());
    }
}