package com.todoapirest.todo_list_api;

import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListApiApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    //Todo creation tests
	@Test
	void creationSuccess() {
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

}
