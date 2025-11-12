package com.todoapirest.todo_list_api.Service;
import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Exceptions.Records.ErrorResponse;
import com.todoapirest.todo_list_api.Repository.TodoRepository;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoCreateRequest;
import com.todoapirest.todo_list_api.TodoDataTransferObject.TodoUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    public TodoService(TodoRepository todoRepository) {this.todoRepository = todoRepository;}


    public Optional<Todo>  createTodo(TodoCreateRequest todoRequest){
        try {
            Todo todo = new Todo();
            todo.setTitle(todoRequest.title());
            todo.setCompleted(todoRequest.completed());
            todo.setDescription(todoRequest.description());
            todo.setPriority(todoRequest.priority());
            return Optional.of(todoRepository.save(todo));
        } catch (Exception ex) {
            throw new RuntimeException("Error for toDo creation on createTodo() method.", ex);
        }
    }

    public Optional<Todo> updateTodo(TodoUpdateRequest todo){
        try{
            return todoRepository.findById(todo.id())
                    .map(existing -> {
                        existing.setTitle(todo.title());
                        existing.setCompleted(todo.completed());
                        existing.setDescription(todo.description());
                        existing.setPriority(todo.priority());
                        return todoRepository.save(existing);
                    });
        } catch (Exception ex) {
            throw new RuntimeException("Error for toDo update on updateTodo() method.", ex);
        }
    }

    public Optional<Todo> listTodo(Long id){
        try{
            return todoRepository.findById(id);
        } catch (Exception ex) {
            throw new RuntimeException("Error for recover todo on listTodo() method.", ex);
        }
    }

    public Optional<List<Todo>> listAllTodos(){
        try{
            return Optional.of(todoRepository.findAll());
        } catch (Exception ex) {
            throw new RuntimeException("Error for toDo listing on listAllTodos() method.", ex);
        }
    }

    public Optional<Todo> deleteTodo(Long id){
        try{
            Optional<Todo> todo = listTodo(id);
            if(todo.isPresent()){
                todoRepository.deleteById(id);
                return todo;
            }
            return Optional.empty();
        } catch (Exception ex) {
            throw new RuntimeException("Error for toDo deletion on deleteTodo() method.", ex);
        }
    }
}
