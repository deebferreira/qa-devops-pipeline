package com.qapipeline.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskCreationApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    private String generateText(int length) {
        return "A".repeat(length);
    }

    // TS-001
    @Test
    @DisplayName("TS-001 - Criar tarefa com título e prioridade válidos")
    void ts001_shouldCreateTaskWithValidData() {

        String requestBody = """
                {
                    "title": "Estudar REST Assured",
                    "description": "Criar testes automatizados da API",
                    "priority": "HIGH"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Estudar REST Assured"))
                .body("description", equalTo("Criar testes automatizados da API"))
                .body("priority", equalTo("HIGH"))
                .body("status", equalTo("TODO"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    // TS-002
    @Test
    @DisplayName("TS-002 - Criar tarefa sem informar o título")
    void ts002_shouldRejectTaskWithoutTitle() {

        String requestBody = """
                {
                    "description": "Teste de criação sem informar o título",
                    "priority": "HIGH"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }

    // TS-003
    @Test
    @DisplayName("TS-003 - Criar tarefa com título vazio")
    void ts003_shouldRejectTaskWithEmptyTitle() {

        String requestBody = """
                {
                    "title": "",
                    "description": "Teste com título vazio",
                    "priority": "HIGH"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }

    // TS-004
    @Test
    @DisplayName("TS-004 - Criar tarefa com título contendo exatamente 100 caracteres")
    void ts004_shouldCreateTaskWithTitleAtMaximumLength() {

        String title = generateText(100);

        String requestBody = """
                {
                    "title": "%s",
                    "description": "Teste de título com 100 caracteres",
                    "priority": "MEDIUM"
                }
                """.formatted(title);

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("title", equalTo(title));
    }

    // TS-005
    @Test
    @DisplayName("TS-005 - Criar tarefa com título contendo 101 caracteres")
    void ts005_shouldRejectTaskWithTitleAboveMaximumLength() {

        String title = generateText(101);

        String requestBody = """
                {
                    "title": "%s",
                    "description": "Teste de título acima do limite",
                    "priority": "MEDIUM"
                }
                """.formatted(title);

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }

    // TS-006
    @Test
    @DisplayName("TS-006 - Criar tarefa sem prioridade")
    void ts006_shouldRejectTaskWithoutPriority() {

        String requestBody = """
                {
                    "title": "Teste sem prioridade",
                    "description": "Validação do campo obrigatório priority"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }

    // TS-007
    @Test
    @DisplayName("TS-007 - Criar tarefa com prioridade LOW")
    void ts007_shouldCreateTaskWithLowPriority() {

        String requestBody = """
                {
                    "title": "Tarefa prioridade LOW",
                    "description": "Teste da prioridade LOW",
                    "priority": "LOW"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("priority", equalTo("LOW"));
    }

    // TS-008
    @Test
    @DisplayName("TS-008 - Criar tarefa com prioridade MEDIUM")
    void ts008_shouldCreateTaskWithMediumPriority() {

        String requestBody = """
                {
                    "title": "Tarefa prioridade MEDIUM",
                    "description": "Teste da prioridade MEDIUM",
                    "priority": "MEDIUM"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("priority", equalTo("MEDIUM"));
    }

    // TS-009
    @Test
    @DisplayName("TS-009 - Criar tarefa com prioridade HIGH")
    void ts009_shouldCreateTaskWithHighPriority() {

        String requestBody = """
                {
                    "title": "Tarefa prioridade HIGH",
                    "description": "Teste da prioridade HIGH",
                    "priority": "HIGH"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("priority", equalTo("HIGH"));
    }

    // TS-010
    @Test
    @DisplayName("TS-010 - Criar tarefa com prioridade inválida")
    void ts010_shouldRejectTaskWithInvalidPriority() {

        String requestBody = """
                {
                    "title": "Tarefa prioridade inválida",
                    "description": "Teste com prioridade não permitida",
                    "priority": "URGENT"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }

    // TS-011
    @Test
    @DisplayName("TS-011 - Validar status inicial TODO")
    void ts011_shouldCreateTaskWithTodoStatusByDefault() {

        String requestBody = """
                {
                    "title": "Validar status inicial",
                    "description": "Teste sem informar status",
                    "priority": "MEDIUM"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("status", equalTo("TODO"));
    }

    // TS-012
    @Test
    @DisplayName("TS-012 - Criar tarefa sem descrição")
    void ts012_shouldCreateTaskWithoutDescription() {

        String requestBody = """
                {
                    "title": "Tarefa sem descrição",
                    "priority": "MEDIUM"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Tarefa sem descrição"))
                .body("priority", equalTo("MEDIUM"))
                .body("status", equalTo("TODO"));
    }

    // TS-013
    @Test
    @DisplayName("TS-013 - Criar tarefa com descrição contendo exatamente 500 caracteres")
    void ts013_shouldCreateTaskWithDescriptionAtMaximumLength() {

        String description = generateText(500);

        String requestBody = """
                {
                    "title": "Teste descrição com 500 caracteres",
                    "description": "%s",
                    "priority": "MEDIUM"
                }
                """.formatted(description);

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("description", equalTo(description));
    }

    // TS-014
    @Test
    @DisplayName("TS-014 - Criar tarefa com descrição contendo 501 caracteres")
    void ts014_shouldRejectTaskWithDescriptionAboveMaximumLength() {

        String description = generateText(501);

        String requestBody = """
                {
                    "title": "Teste descrição acima do limite",
                    "description": "%s",
                    "priority": "MEDIUM"
                }
                """.formatted(description);

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }
}