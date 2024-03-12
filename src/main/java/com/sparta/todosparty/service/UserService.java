package com.sparta.todosparty.service;

import com.sparta.todosparty.dto.request.LoginRequestDTO;
import com.sparta.todosparty.dto.request.SignupRequestDTO;
import com.sparta.todosparty.entity.User;
import com.sparta.todosparty.entity.UserRole;
import com.sparta.todosparty.jwt.JwtUtil;
import com.sparta.todosparty.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public User signupUser(SignupRequestDTO signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        validateUsernameAndPassword(username, password);

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        UserRole role = UserRole.USER;
        if (signupRequestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

    public String loginUser(LoginRequestDTO loginRequestDTO) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequestDTO.getUsername());

        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("입력한 사용자를 찾을 수 없습니다."));

        if (passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            return jwtUtil.createToken(user.getUsername(), user.getRole());
        } else {
            throw new BadCredentialsException("잘못된 비밀번호입니다. 다시 시도해주세요.");
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("입력한 사용자를 찾을 수 없습니다."));
    }

    private void validateUsernameAndPassword(String username, String password) {
        if (username.length() < 4 || username.length() > 10 || !username.matches("^[a-z0-9]+$")) {
            throw new IllegalArgumentException("username은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 이루어져야 합니다.");
        }

        if (password.length() < 8 || password.length() > 15 || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]+$")) {
            throw new IllegalArgumentException("password는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 이루어져야 합니다.");
        }
    }
}

