package com.sparta.todosparty.repository;

import com.sparta.todosparty.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
