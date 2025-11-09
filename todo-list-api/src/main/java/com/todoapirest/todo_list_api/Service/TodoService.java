package com.todoapirest.todo_list_api.Service;
import com.todoapirest.todo_list_api.Entity.Todo;
import com.todoapirest.todo_list_api.Repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    public TodoService(TodoRepository todoRepository) {this.todoRepository = todoRepository;}


    public Todo createTodo(Todo todo){
       return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Todo todo){
       return todoRepository.findById(todo.getId());
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
