package com.example.demo.controller;

import com.example.demo.dto.TodoDTO;
import com.example.demo.entity.TodoEntity;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TodoControllerTest {
    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    private static final String userid = "user";

    @Test
    @DisplayName("Todo 생성 테스트")
    void createTodo() {
        TodoDTO dto = TodoDTO.builder()
                .id("id")
                .title("title")
                .done(true)
                .build();

        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(userid);

        List<TodoEntity> todo = todoService.createTodo(entity);

        assertNotNull(todo.get(0).getId());
        assertEquals(entity.getUserId(), todo.get(0).getUserId());
    }

    @Test
    @DisplayName("userId의 Todo 리스트 조회 테스트")
    void readTodoList() {
        List<TodoEntity> entityList = todoRepository.saveAll(List.of(
                TodoEntity.builder()
                        .id("1")
                        .userId(userid)
                        .title("title 1")
                        .done(true)
                        .build(),
                TodoEntity.builder()
                        .id("2")
                        .userId(userid)
                        .title("title 2")
                        .done(false)
                        .build()
        ));

        List<TodoDTO> list = todoService.readTodoList(userid);

        assertEquals(list.get(0).getId(), entityList.get(0).getId());
    }

    @Test
    @DisplayName("Todo 수정 테스트")
    void updateTodo() {
    }

    @Test
    void deleteTodo() {
    }
}