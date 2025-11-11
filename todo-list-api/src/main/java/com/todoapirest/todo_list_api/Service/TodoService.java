package com.todoapirest.todo_list_api.Service;
import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Repository.TodoRepository;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
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

        return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Todo todo){
        return Optional.of(todoRepository.findById(todo.getId())
                .map(existing -> {
                    existing.setTitle(todo.getTitle());
                    existing.setCompleted(todo.getCompleted());
                    existing.setDescription(todo.getDescription());
                    existing.setPriority(todo.getPriority());
                    return todoRepository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException("Todo not found: " + todo.getId())));
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
