package com.sparta.todosparty.dto.response;

import com.sparta.todosparty.dto.request.UserDTO;
import com.sparta.todosparty.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDTO extends TodosResponseDTO {

    private Long id;
    private String commentContent;
    private UserDTO user;
    private LocalDateTime createDate;

    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.commentContent = comment.getCommentContent();
        this.user = new UserDTO(comment.getUser());
        this.createDate = comment.getCreateDate();
    }
}
