package com.example.demo.controller;

import com.example.demo.dto.TodoDTO;
import com.example.demo.entity.TodoEntity;
import com.example.demo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @MockBean
    private TodoService todoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String userid = "temporary-user";

    @Test
    @DisplayName("Todo 생성 테스트")
    void createTodo() throws Exception {
        TodoDTO dto = TodoDTO.builder()
                .id("id")
                .title("title")
                .done(true)
                .build();

        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(userid);

        given(todoService.createTodo(any())).willReturn(List.of(entity));

        String requestJson = objectMapper.writeValueAsString(entity);

        mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title"));
    }

    @Test
    @DisplayName("userId의 Todo 리스트 조회 테스트")
    void readTodoList() throws Exception {
        List<TodoEntity> entityList = List.of(
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
        );

        List<TodoDTO> list = entityList.stream().map(TodoDTO::new).toList();

        given(todoService.readTodoList(userid)).willReturn(list);

        mockMvc.perform(get("/todo/list?userId=" + userid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title 1"));
    }

    @Test
    @DisplayName("Todo 수정 테스트")
    void updateTodo() throws Exception {
        TodoEntity beforeEntity = TodoEntity.builder()
                .id("123")
                .userId(userid)
                .title("title 1")
                .done(true)
                .build();
        TodoEntity afterEntity = TodoEntity.builder()
                .id(beforeEntity.getId())
                .userId(userid)
                .title("title 2")
                .done(false)
                .build();

        TodoDTO todoDTO = new TodoDTO(afterEntity);
        given(todoService.updateTodo(any())).willReturn(todoDTO);

        String requestJson = objectMapper.writeValueAsString(todoDTO);

        mockMvc.perform(post("/todo/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title 2"));
    }

    @Test
    @DisplayName("Todo 삭제 테스트")
    void deleteTodo() throws Exception {
        TodoEntity entity = TodoEntity.builder()
                        .id("did")
                        .userId(userid)
                        .title("title 133")
                        .done(false)
                .build();

        TodoDTO dto = new TodoDTO(entity);

        given(todoService.deleteTodo(entity.getId())).willReturn(true);

        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(delete("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value(true));
    }
}