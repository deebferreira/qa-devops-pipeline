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

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskListingApiTest {

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

    // TS-015
    @Test
    @DisplayName("TS-015 - Consultar lista com tarefas cadastradas")
    void ts015_shouldListExistingTasks() {

        String firstTask = """
                {
                    "title": "Primeira tarefa da listagem",
                    "description": "Tarefa criada para validar GET /tasks",
                    "priority": "HIGH"
                }
                """;

        String secondTask = """
                {
                    "title": "Segunda tarefa da listagem",
                    "description": "Segunda tarefa criada para validar a listagem",
                    "priority": "LOW"
                }
                """;

        // Arrange - cria a primeira tarefa
        given()
                .contentType("application/json")
                .body(firstTask)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201);

        // Arrange - cria a segunda tarefa
        given()
                .contentType("application/json")
                .body(secondTask)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201);

        // Act - consulta todas as tarefas
        Response response = given()
                .when()
                .request("GET", "/tasks");

        // Assert
        assertEquals(200, response.statusCode());

        List<Map<String, Object>> tasks =
                response.jsonPath().getList("$");

        assertEquals(2, tasks.size());

        boolean firstTaskExists = tasks.stream()
                .anyMatch(task ->
                        "Primeira tarefa da listagem".equals(task.get("title")));

        boolean secondTaskExists = tasks.stream()
                .anyMatch(task ->
                        "Segunda tarefa da listagem".equals(task.get("title")));

        assertTrue(firstTaskExists);
        assertTrue(secondTaskExists);
    }

    // TS-016
    @Test
    @DisplayName("TS-016 - Consultar tarefas quando não existem registros")
    void ts016_shouldReturnEmptyListWhenThereAreNoTasks() {

        // Act
        Response response = given()
                .when()
                .request("GET", "/tasks");

        // Assert
        assertEquals(200, response.statusCode());

        List<Map<String, Object>> tasks =
                response.jsonPath().getList("$");

        assertTrue(tasks.isEmpty());
    }
}