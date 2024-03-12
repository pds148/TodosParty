package com.sparta.todosparty.dto.response;

import com.sparta.todosparty.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class TodoResponseDTO {

    private Long id;
    private String todoTitle;
    private String todoContent;
    private Boolean isCompleted;

    public TodoResponseDTO(String message, Integer statusCode) {
        super();
    }

    public TodoResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.todoTitle = todo.getTodoTitle();
        this.todoContent = todo.getTodoContent();
        this.isCompleted = todo.getIsCompleted();

    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public void setTodoContent(String todoContent) {
        this.todoContent = todoContent;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
