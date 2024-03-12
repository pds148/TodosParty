package com.sparta.todosparty.dto.request;

import com.sparta.todosparty.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UserDTO {
    private String username;

    public UserDTO(User user) {
        this.username = user.getUsername();
    }
}