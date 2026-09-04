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
class TaskStatusApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Garante que cada teste comece com o banco limpo
        taskRepository.deleteAll();
    }

    private Long createTask() {

        String requestBody = """
                {
                    "title": "Tarefa para alteração de status",
                    "description": "Tarefa criada para os testes do RF-005",
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

        assertEquals(
                "TODO",
                response.jsonPath().getString("status")
        );

        return taskId;
    }

    // TS-023
    @Test
    @DisplayName("TS-023 - Alterar status de TODO para IN_PROGRESS")
    void ts023_shouldChangeStatusFromTodoToInProgress() {

        Long taskId = createTask();

        String requestBody = """
                {
                    "status": "IN_PROGRESS"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .patch("/tasks/" + taskId + "/status");

        assertEquals(200, response.statusCode());

        assertEquals(
                taskId,
                response.jsonPath().getLong("id")
        );

        assertEquals(
                "IN_PROGRESS",
                response.jsonPath().getString("status")
        );

        assertEquals(
                "Tarefa para alteração de status",
                response.jsonPath().getString("title")
        );
    }

    // TS-024
    @Test
    @DisplayName("TS-024 - Alterar status de IN_PROGRESS para DONE")
    void ts024_shouldChangeStatusFromInProgressToDone() {

        Long taskId = createTask();

        String inProgressBody = """
                {
                    "status": "IN_PROGRESS"
                }
                """;

        Response inProgressResponse = given()
                .contentType("application/json")
                .body(inProgressBody)
                .when()
                .patch("/tasks/" + taskId + "/status");

        assertEquals(200, inProgressResponse.statusCode());

        assertEquals(
                "IN_PROGRESS",
                inProgressResponse.jsonPath().getString("status")
        );

        String doneBody = """
                {
                    "status": "DONE"
                }
                """;

        Response doneResponse = given()
                .contentType("application/json")
                .body(doneBody)
                .when()
                .patch("/tasks/" + taskId + "/status");

        assertEquals(200, doneResponse.statusCode());

        assertEquals(
                taskId,
                doneResponse.jsonPath().getLong("id")
        );

        assertEquals(
                "DONE",
                doneResponse.jsonPath().getString("status")
        );
    }

    // TS-025
    @Test
    @DisplayName("TS-025 - Alterar tarefa para status inválido")
    void ts025_shouldRejectInvalidStatus() {

        Long taskId = createTask();

        String requestBody = """
                {
                    "status": "CANCELLED"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .patch("/tasks/" + taskId + "/status");

        assertEquals(400, response.statusCode());

        // Confirma que o status original não foi alterado
        Response taskResponse = given()
                .when()
                .request("GET", "/tasks/" + taskId);

        assertEquals(200, taskResponse.statusCode());

        assertEquals(
                "TODO",
                taskResponse.jsonPath().getString("status")
        );
    }

    // TS-026
    @Test
    @DisplayName("TS-026 - Alterar status utilizando ID inexistente")
    void ts026_shouldReturn404WhenChangingStatusOfNonexistentTask() {

        Long nonexistentId = 999999L;

        String requestBody = """
                {
                    "status": "DONE"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .patch("/tasks/" + nonexistentId + "/status");

        assertEquals(404, response.statusCode());

        boolean exists = taskRepository.existsById(nonexistentId);

        assertEquals(false, exists);
    }
}