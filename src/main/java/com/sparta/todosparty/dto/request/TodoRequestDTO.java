package com.sparta.todosparty.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class TodoRequestDTO {

    private String todoTitle;
    private String todoContent;

    public TodoRequestDTO(String todoTitle, String todoContent) {
        this.todoTitle = todoTitle;
        this.todoContent = todoContent;
    }
}
