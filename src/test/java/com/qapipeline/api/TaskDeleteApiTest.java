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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskDeleteApiTest {

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

    private Long createTask() {

        String requestBody = """
                {
                    "title": "Tarefa para exclusão",
                    "description": "Tarefa criada para os testes do RF-006",
                    "priority": "MEDIUM"
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

    // TS-027
    @Test
    @DisplayName("TS-027 - Excluir tarefa existente")
    void ts027_shouldDeleteExistingTask() {

        Long taskId = createTask();

        Response response = given()
                .when()
                .delete("/tasks/" + taskId);

        assertEquals(204, response.statusCode());

        assertFalse(taskRepository.existsById(taskId));
    }

    // TS-028
    @Test
    @DisplayName("TS-028 - Tarefa não deve existir após exclusão")
    void ts028_shouldReturn404WhenGettingDeletedTask() {

        Long taskId = createTask();

        Response deleteResponse = given()
                .when()
                .delete("/tasks/" + taskId);

        assertEquals(204, deleteResponse.statusCode());

        Response getResponse = given()
                .when()
                .request("GET", "/tasks/" + taskId);

        assertEquals(404, getResponse.statusCode());

        assertFalse(taskRepository.existsById(taskId));
    }

    // TS-029
    @Test
    @DisplayName("TS-029 - Excluir tarefa inexistente")
    void ts029_shouldReturn404WhenDeletingNonexistentTask() {

        Long nonexistentId = 999999L;

        Response response = given()
                .when()
                .delete("/tasks/" + nonexistentId);

        assertEquals(404, response.statusCode());

        assertFalse(taskRepository.existsById(nonexistentId));
    }
}