package com.todoapirest.todo_list_api.Controller;

import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Service.TodoService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private TodoService todoService;
    public TodoController(TodoService todoService) {this.todoService = todoService;}

    @PostMapping
    public Todo create(@RequestBody Todo todo){
        return todoService.createTodo(todo);
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
