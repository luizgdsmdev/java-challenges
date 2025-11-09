package com.todoapirest.todo_list_api.Repository;

import com.todoapirest.todo_list_api.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {}
