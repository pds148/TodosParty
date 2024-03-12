package com.sparta.todosparty.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDTO {
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String Password) {
        this.password = password;
    }
}
