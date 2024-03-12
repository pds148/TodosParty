package com.sparta.todosparty.controller;

import com.sparta.todosparty.dto.request.CommentRequestDTO;
import com.sparta.todosparty.dto.response.CommentResponseDTO;
import com.sparta.todosparty.dto.response.TodosResponseDTO;
import com.sparta.todosparty.security.UserDetailsImpl;
import com.sparta.todosparty.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> postComment(@RequestBody CommentRequestDTO commentRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDTO responseDTO = commentService.createComment(commentRequestDTO, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<TodosResponseDTO> putComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            CommentResponseDTO responseDTO = commentService.updateComment(commentId, commentRequestDTO, userDetails.getUser());
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new TodosResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<TodosResponseDTO> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            commentService.deleteComment(commentId, userDetails.getUser());
            return ResponseEntity.ok().body(new TodosResponseDTO("정상적으로 삭제 되었습니다.", HttpStatus.OK.value()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new TodosResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
