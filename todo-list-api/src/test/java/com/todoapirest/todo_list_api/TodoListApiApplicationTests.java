package com.todoapirest.todo_list_api;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListApiApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    //Todo creation tests
    @DisplayName("POST: /todo -> Create succeeds with valid data")
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


    @DisplayName("POST: /todo -> Create succeeds with valid data, but empty description")
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

}
