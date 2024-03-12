package com.sparta.todosparty.controller;

import com.sparta.todosparty.service.TodoService;
import com.sparta.todosparty.dto.request.TodoRequestDTO;
import com.sparta.todosparty.dto.response.TodoListResponseDTO;
import com.sparta.todosparty.dto.response.TodoResponseDTO;
import com.sparta.todosparty.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponseDTO> addTodo(
            @RequestBody TodoRequestDTO todoRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TodoResponseDTO todoResponseDTO = todoService.createTodo(todoRequestDTO, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(todoResponseDTO);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponseDTO> getTodoDetails(@PathVariable Long todoId) {
        try {
            TodoResponseDTO responseDTO = todoService.getTodoDetails(todoId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TodoResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping
    public ResponseEntity<List<TodoListResponseDTO>> getUserTodoList() {
        List<TodoListResponseDTO> response = todoService.getUserTodoList();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponseDTO> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TodoRequestDTO todoRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            TodoResponseDTO responseDTO = todoService.updateTodo(todoId, todoRequestDTO, userDetails.getUser());
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TodoResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PatchMapping("/{todoId}/complete")
    public ResponseEntity<TodoResponseDTO> completeTodo(
            @PathVariable Long todoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            TodoResponseDTO responseDTO = todoService.completeTodo(todoId, userDetails.getUser());
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TodoResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(
            @PathVariable Long todoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            todoService.deleteTodo(todoId, userDetails.getUser());
            return ResponseEntity.ok("할일이 삭제되었습니다.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
