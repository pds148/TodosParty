package com.sparta.todosparty.service;

import com.sparta.todosparty.dto.request.TodoRequestDTO;
import com.sparta.todosparty.dto.request.UserDTO;
import com.sparta.todosparty.dto.response.TodoListResponseDTO;
import com.sparta.todosparty.dto.response.TodoResponseDTO;
import com.sparta.todosparty.entity.Todo;
import com.sparta.todosparty.entity.User;
import com.sparta.todosparty.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDTO createTodo(TodoRequestDTO todoRequestDTO, User user) {
        Todo todo = new Todo(todoRequestDTO);
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDTO(savedTodo);
    }

    public TodoResponseDTO getTodoDetails(Long todoId) {
        Todo todo = getTodoById(todoId);
        return new TodoResponseDTO(todo);
    }

    public List<TodoListResponseDTO> getUserTodoList() {
        List<Todo> todoList = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        Map<UserDTO, List<TodoResponseDTO>> userTodoMap = todoList.stream()
                .collect(Collectors.groupingBy(todo -> new UserDTO(todo.getUser()),
                        Collectors.mapping(TodoResponseDTO::new, Collectors.toList())));

        return userTodoMap.entrySet().stream()
                .map(entry -> new TodoListResponseDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional
    public TodoResponseDTO updateTodo(Long todoId, TodoRequestDTO todoRequestDTO, User user) {
        Todo todo = getUserTodoById(todoId, user);

        todo.setTitle(todoRequestDTO.getTodoTitle());
        todo.setContent(todoRequestDTO.getTodoContent());

        return new TodoResponseDTO(todo);
    }

    @Transactional
    public TodoResponseDTO completeTodo(Long todoId, User user) {
        Todo todo = getUserTodoById(todoId, user);
        todo.complete();
        return new TodoResponseDTO(todo);
    }

    @Transactional
    public void deleteTodo(Long todoId, User user) {
        Todo todo = getUserTodoById(todoId, user);
        todoRepository.delete(todo);
    }

    public Todo getTodoById(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할일 ID입니다."));
    }

    public Todo getUserTodoById(Long todoId, User user) {
        Todo todo = getTodoById(todoId);
        if (!user.getId().equals(todo.getUser().getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        return todo;
    }

}
