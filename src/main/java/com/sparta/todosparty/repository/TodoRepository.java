package com.sparta.todosparty.repository;

import com.sparta.todosparty.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
