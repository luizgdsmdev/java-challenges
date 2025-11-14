package com.todoapirest.todo_list_api.Controller;
import com.todoapirest.todo_list_api.Exceptions.Records.ErrorResponse;
import com.todoapirest.todo_list_api.Service.TodoService;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private TodoService todoService;
    public TodoController(TodoService todoService) {this.todoService = todoService;}

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid TodoCreateRequest todoRequest){
        return todoService.createTodo(todoRequest)
        .map(newTodo -> {
            URI location = URI.create("/todo/" + newTodo.getId());
            return ResponseEntity.created(location).body((Object) newTodo);
        })
        .orElseGet(() -> ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
                "Bad Request",
                "Malformed JSON in request body or invalid/non-existent id",
                LocalDateTime.now()
            ))
        );
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @Valid TodoUpdateRequest todo) {
        return todoService.updateTodo(todo)
        .map(updated -> ResponseEntity.status(HttpStatus.OK).body((Object) updated))
        .orElseGet(() -> ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
                "Bad Request",
                "Malformed JSON in request body or invalid/non-existent id",
                LocalDateTime.now()
            ))
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return todoService.listAllTodos()
        .map(todoList -> ResponseEntity.status(HttpStatus.OK).body((Object) todoList))
        .orElseGet(() -> ResponseEntity
        .status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ErrorResponse(
                "Unprocessable Entity",
                "Malformed JSON in request body or invalid/non-existent id",
                LocalDateTime.now()
            ))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTodo(@PathVariable Long id){
        return todoService.listTodo(id)
        .map(todo -> ResponseEntity.ok().body( (Object) todo))
        .orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "Bad Request",
                        "Invalid or non-existent id",
                        LocalDateTime.now()
                ))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTodo(@PathVariable Long id){
        return todoService.deleteTodo(id)
        .map(todo -> ResponseEntity.accepted().body((Object) todo))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                    "Bad Request",
                    "Invalid or non-existent id",
                    LocalDateTime.now()
            ))
        );
    }





}
