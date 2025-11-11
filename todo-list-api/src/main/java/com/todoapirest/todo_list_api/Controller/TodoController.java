package com.todoapirest.todo_list_api.Controller;

import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Service.TodoService;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public Optional<Todo> update(@RequestBody Todo todo){
        return todoService.updateTodo(todo);
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
