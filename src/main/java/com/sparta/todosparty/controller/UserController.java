package com.sparta.todosparty.controller;

import com.sparta.todosparty.dto.request.LoginRequestDTO;
import com.sparta.todosparty.dto.request.SignupRequestDTO;
import com.sparta.todosparty.dto.response.TodosResponseDTO;
import com.sparta.todosparty.entity.User;
import com.sparta.todosparty.entity.UserRole;
import com.sparta.todosparty.jwt.JwtUtil;
import com.sparta.todosparty.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<TodosResponseDTO> postSignup(
            @Valid @RequestBody SignupRequestDTO signupRequestDto, BindingResult bindingResult) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : fieldErrors) {
                log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        try {
            userService.signupUser(signupRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TodosResponseDTO("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new TodosResponseDTO("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<TodosResponseDTO> postLogin(
            @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        try {
            userService.loginUser(loginRequestDTO);
            User user = userService.getUserByUsername(loginRequestDTO.getUsername());
            String username = user.getUsername();
            UserRole role = user.getRole();

            String token = jwtUtil.createToken(username, role);
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TodosResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new TodosResponseDTO("로그인 성공", HttpStatus.OK.value()));
    }
}

