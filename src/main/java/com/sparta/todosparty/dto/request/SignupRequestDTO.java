package com.sparta.todosparty.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class SignupRequestDTO {

    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    @NotBlank
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]+$")
    @NotBlank
    private String password;

    private boolean admin = false;
    private String adminToken = "";

    public User toEntity() {
        return (User) User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
