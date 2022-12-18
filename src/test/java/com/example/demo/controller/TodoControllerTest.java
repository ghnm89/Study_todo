package com.example.demo.controller;

import com.example.demo.dto.TodoDTO;
import com.example.demo.entity.TodoEntity;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoControllerTest {
    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    private static final String userid = "temporary-user";

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
        TodoEntity beforeEntity = todoRepository.save(TodoEntity.builder()
                .id("123")
                .userId(userid)
                .title("title 1")
                .done(true)
                .build());
        TodoEntity afterEntity = TodoEntity.builder()
                .id(beforeEntity.getId())
                .userId(userid)
                .title("title 2")
                .done(false)
                .build();

        TodoDTO todoDTO = todoService.updateTodo(afterEntity);

        assertEquals(todoDTO.getId(), afterEntity.getId());
        assertEquals(todoDTO.getTitle(), afterEntity.getTitle());
        assertEquals(todoDTO.getDone(), afterEntity.getDone());
    }

    @Test
    @DisplayName("Todo 삭제 테스트")
    void deleteTodo() {
        TodoEntity entity = todoRepository.save(TodoEntity.builder()
                        .id("did")
                        .userId(userid)
                        .title("title 133")
                        .done(false)
                .build());

        Boolean isDelete = todoService.deleteTodo(entity.getId());

        assertTrue(isDelete);
    }
}