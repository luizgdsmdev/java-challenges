package com.todoapirest.todo_list_api.Controller;

import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Exceptions.Records.ErrorResponse;
import com.todoapirest.todo_list_api.Service.TodoService;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private TodoService todoService;
    public TodoController(TodoService todoService) {this.todoService = todoService;}

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody @Valid TodoCreateRequest todoRequest){
        Todo todoBody = todoService.createTodo(todoRequest);
        URI location = URI.create("/todo/" + todoBody.getId());
        return ResponseEntity.created(location).body(todoBody);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @Valid TodoUpdateRequest todo) {
        return todoService.updateTodo(todo)
        .map(updated -> ResponseEntity.status(HttpStatus.OK).body((Object) updated))
        .orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "Todo not found",
                        "ID " + todo.id() + " not found or don't exist, check for a valid payload",
                        LocalDateTime.now()
                ))
        );
    }

    @GetMapping("/all")
    public List<Todo> getAll() {
        return todoService.listAllTodos();
    }

    @GetMapping("/{id}")
    public Optional<Todo> getTodo(@PathVariable Long id){
        return todoService.listTodo(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteTodo(@PathVariable Long id){
        return todoService.deleteTodo(id);
    }
}
