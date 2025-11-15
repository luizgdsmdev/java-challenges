package com.todoapirest.todo_list_api;
import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Repository.TodoRepository;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.matchesPattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class TodoListApiApplicationTests {
    @Autowired
    private WebTestClient webTestClient;
    private Long createdTodoId;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private Environment env;

    @Test
    void debugProfile() {
        System.out.println("=== PERFIS ATIVOS ===");
        Arrays.stream(env.getActiveProfiles()).forEach(p -> System.out.println("→ " + p));

        System.out.println("=== DATABASE URL ===");
        System.out.println(env.getProperty("spring.datasource.url"));
    }

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        TodoCreateRequest create = new TodoCreateRequest("Initial Title", false, "desc", 1);

        URI location = webTestClient.post()
                .uri("/todo")
                .bodyValue(create)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", ".*/\\d+")
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation();

        this.createdTodoId = Long.parseLong(
                location.getPath().substring(location.getPath().lastIndexOf("/") + 1)
        );
    }

    //-------------------------- creation tests: succeeds
    @DisplayName("POST: /todo -> (201 created) | Creation succeeds with valid data.")
	@Test
	void shouldCreateTodo_WhenValidData() {
        TodoCreateRequest requestBody = new TodoCreateRequest("Title", false, "description", 3);

        webTestClient
        .post()
        .uri("/todo")
        .bodyValue(requestBody)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.title").isEqualTo("Title")
        .jsonPath("$.description").isEqualTo("description")
        .jsonPath("$.completed").isEqualTo(false)
        .jsonPath("$.priority").isEqualTo(3)
        .jsonPath("$.id").isNotEmpty();
	}

    @DisplayName("POST: /todo -> (201 created) | Creation succeeds with valid data, but empty description.")
    @Test
    void shouldCreateTodo_WhenDescriptionIsEmpty() {
        TodoCreateRequest requestBody = new TodoCreateRequest("Title", false, "", 3);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Title")
                .jsonPath("$.description").isEqualTo("")
                .jsonPath("$.completed").isEqualTo(false)
                .jsonPath("$.priority").isEqualTo(3)
                .jsonPath("$.id").isNotEmpty();
    }


    //-------------------------- creation tests: failure
    @DisplayName("POST: /todo -> (400 Bad Request) | Creation fails when title value is greater than 60 characters.")
    @Test
    void creationFails_WhenTitleIsGreaterThan60Characters() {
        TodoCreateRequest requestBody = new TodoCreateRequest("1234567890123456789012345678901234567890123456789012345678901234567890", false, "Description", 3);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("title: Title must be between 3 and 60 characters")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("POST: /todo -> (400 Bad Request) | Creation fails when title value is lesser than 3 characters.")
    @Test
    void creationFails_WhenTitleIsLesserThan3Characters() {
        TodoCreateRequest requestBody = new TodoCreateRequest("12", false, "Description", 3);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("title: Title must be between 3 and 60 characters")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("POST: /todo -> (400 Bad Request) | Creation fails when title value is empty.")
    @Test
    void creationFails_WhenTitleIsEmpty() {
        TodoCreateRequest requestBody = new TodoCreateRequest("12", false, "Description", 3);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("title: Title must be between 3 and 60 characters")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("POST: /todo -> (400 Bad Request) | Creation fails when description value is greater than 400 characters.")
    @Test
    void creationFails_WhenDescriptionIsGreaterThan400Characters() {
        TodoCreateRequest requestBody = new TodoCreateRequest("Title", false, "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901", 3);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("description: Description must be 400 characters maximum")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("POST: /todo -> (400 Bad Request) | Creation fails when priority value is greater than 5.")
    @Test
    void creationFails_WhenPriorityIsGreaterThan5() {
        TodoCreateRequest requestBody = new TodoCreateRequest("Title", false, "description", 6);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("priority: Priority field must be between 1 and 5")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("POST: /todo -> (400 Bad Request) | Creation fails when priority value is lesser than 1.")
    @Test
    void creationFails_WhenPriorityIsLesserThan1() {
        TodoCreateRequest requestBody = new TodoCreateRequest("Title", false, "description", 0);

        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("priority: Priority field must be between 1 and 5")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }


    //-------------------------- update tests: succeeds
    @DisplayName("PUT: /todo -> (200 ok) | Update succeeds with valid data.")
    @Test
    void shouldUpdateTodo_WhenValidData() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "New title", true, "New desc", 4
        );

        webTestClient.put()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(createdTodoId.intValue())
                .jsonPath("$.description").isEqualTo("New desc")
                .jsonPath("$.priority").isEqualTo(4)
                .jsonPath("$.completed").isEqualTo(true)
                .jsonPath("$.title").isEqualTo("New title");
    }

    @DisplayName("PUT: /todo -> (200 ok) | Update succeeds with valid data, but empty description.")
    @Test
    void shouldUpdateTodo_WhenDescriptionIsEmpty() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "New title", true, "", 4
        );

        webTestClient.put()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(createdTodoId.intValue())
                .jsonPath("$.description").isEqualTo("")
                .jsonPath("$.priority").isEqualTo(4)
                .jsonPath("$.completed").isEqualTo(true)
                .jsonPath("$.title").isEqualTo("New title");
    }


    //-------------------------- update tests: failure
    @DisplayName("PUT: /todo -> (400 Bad Request) | Update fails when title value is greater than 60 characters.")
    @Test
    void updateFails_WhenTitleIsGreaterThan60Characters() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "1234567890123456789012345678901234567890123456789012345678901234567890", true, "", 4
        );
        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("title: Title must be between 3 and 60 characters")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("PUT: /todo -> (400 Bad Request) | Update fails when title value is lesser than 3 characters.")
    @Test
    void updateFails_WhenTitleIsLesserThan3Characters() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "N", true, "", 4
        );
        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("title: Title must be between 3 and 60 characters")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("PUT: /todo -> (400 Bad Request) | Update fails when title value is empty.")
    @Test
    void updateFails_WhenTitleIsEmpty() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "", true, "", 4
        );
        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("title: Title must be between 3 and 60 characters")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("PUT: /todo -> (400 Bad Request) | Update fails when description value is greater than 400 characters.")
    @Test
    void updateFails_WhenDescriptionIsGreaterThan400Characters() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "New title", true, "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901", 4
        );
        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("description: Description must be 400 characters maximum")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("PUT: /todo -> (400 Bad Request) | Update fails when priority value is greater than 5.")
    @Test
    void updateFails_WhenPriorityIsGreaterThan5() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "New title", true, "", 6
        );
        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("priority: Priority field must be between 1 and 5")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }

    @DisplayName("PUT: /todo -> (400 Bad Request) | Update fails when priority value is lesser than 1.")
    @Test
    void updateFails_WhenPriorityIsLesserThan1() {
        TodoUpdateRequest update = new TodoUpdateRequest(
                createdTodoId, "New title", true, "", 0
        );
        webTestClient
                .post()
                .uri("/todo")
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid data")
                .jsonPath("$.details").isEqualTo("priority: Priority field must be between 1 and 5")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }



    //-------------------------- get tests: succeeds
    @DisplayName("GET: /todo -> (200 ok) | get succeeds with valid ID.")
    @Test
    void shouldGetTodo_WhenValidId() {

        webTestClient.get()
                .uri("/todo/" + createdTodoId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(createdTodoId.intValue())
                .jsonPath("$.title").isEqualTo("Initial Title")
                .jsonPath("$.description").isEqualTo("desc")
                .jsonPath("$.priority").isEqualTo(1)
                .jsonPath("$.completed").isEqualTo(false);
    }

    //-------------------------- get tests: failure
    @DisplayName("GET: /todo -> (404 Not Found) | get Not Found with invalid ID.")
    @Test
    void getFails_WhenInvalidId() {

        webTestClient.get()
                .uri("/todo/" + 0)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Bad Request")
                .jsonPath("$.details").isEqualTo("Invalid or non-existent id")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }


    //-------------------------- Delete tests: succeeds
    @DisplayName("DELETE: /todo -> (202 Accepted) | Delete succeeds with valid ID.")
    @Test
    void shouldDeleteTodo_WhenValidId() {

        webTestClient.delete()
                .uri("/todo/" + createdTodoId)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$.id").isEqualTo(createdTodoId.intValue())
                .jsonPath("$.title").isEqualTo("Initial Title")
                .jsonPath("$.description").isEqualTo("desc")
                .jsonPath("$.priority").isEqualTo(1)
                .jsonPath("$.completed").isEqualTo(false);
    }

    //-------------------------- Delete tests: failure
    @DisplayName("DELETE: /todo -> (400 Bad Request) | Delete fails with invalid ID.")
    @Test
    void deleteFails_WhenInvalidId() {

        webTestClient.delete()
                .uri("/todo/" + 0)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Bad Request")
                .jsonPath("$.details").isEqualTo("Invalid or non-existent id")
                .jsonPath("$.timestamp").isNotEmpty()
                .jsonPath("$.timestamp").value(matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"
                ));
    }


    //-------------------------- Get All tests: succeeds
    @DisplayName("GET: /all -> (200 OK) | retorna página com dados reais e metadados de paginação")
    @Test
    void shouldReturnPaginatedTodos_WithRealData() {
        for (int i = 1; i <= 25; i++) {
            Todo t = new Todo();
            t.setTitle("Tarefa " + i);
            t.setDescription("Desc " + i);
            t.setPriority(i % 5);
            t.setCompleted(false);
            todoRepository.save(t);
        }


        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("todo/all")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(10)
                .jsonPath("$.content[0].title").isNotEmpty()
                .jsonPath("$.content[9].title").isNotEmpty()
                .jsonPath("$.pageable.pageNumber").isEqualTo(0)
                .jsonPath("$.pageable.pageSize").isEqualTo(10)
                .jsonPath("$.totalElements").isEqualTo(26)
                .jsonPath("$.totalPages").isEqualTo(3)
                .jsonPath("$.first").isEqualTo(true)
                .jsonPath("$.last").isEqualTo(false)
                .jsonPath("$.numberOfElements").isEqualTo(10);
    }




}
