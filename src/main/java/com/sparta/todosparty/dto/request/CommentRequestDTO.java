package com.sparta.todosparty.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDTO {

    private String commentContent;
    private Long todoId;
}
