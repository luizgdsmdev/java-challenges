package com.todoapirest.todo_list_api.Repository;

import com.todoapirest.todo_list_api.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
