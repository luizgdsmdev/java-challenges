package com.todoapirest.todo_list_api;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.hamcrest.Matchers.matchesPattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListApiApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

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



}
