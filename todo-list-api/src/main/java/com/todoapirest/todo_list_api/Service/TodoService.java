package com.todoapirest.todo_list_api.Service;
import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Repository.TodoRepository;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    public TodoService(TodoRepository todoRepository) {this.todoRepository = todoRepository;}


    public Todo createTodo(TodoCreateRequest todoRequest){
        Todo todo = new Todo();
        todo.setTitle(todoRequest.title());
        todo.setCompleted(todoRequest.completed());
        todo.setDescription(todoRequest.description());
        todo.setPriority(todoRequest.priority());

        try {
            return todoRepository.save(todo);
        } catch (Exception ex) {
            throw new RuntimeException("Error for toDo creation on createTodo() method.", ex);
        }
    }

    public Optional<Todo> updateTodo(TodoUpdateRequest todo){
        return todoRepository.findById(todo.id())
        .map(existing -> {
            existing.setTitle(todo.title());
            existing.setCompleted(todo.completed());
            existing.setDescription(todo.description());
            existing.setPriority(todo.priority());
            return todoRepository.save(existing);
        });
    }

    public Optional<Todo> listTodo(Long id){
        return todoRepository.findById(id);
    }

    public List<Todo> listAllTodos(){
        return todoRepository.findAll();
    }

    public boolean deleteTodo(Long id){
        todoRepository.deleteById(id);

        return true;
    }
}
