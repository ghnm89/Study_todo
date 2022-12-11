package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.entity.TodoEntity;
import com.example.demo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO request) {
        try {
            String temporaryUserId = "temporary-user";

            TodoEntity todoEntity = TodoDTO.toEntity(request);

            todoEntity.setId(null);
            todoEntity.setUserId(temporaryUserId);
            todoEntity.setDone(false);

            List<TodoEntity> entityList = todoService.createTodo(todoEntity);

            List<TodoDTO> dtoList = entityList.stream().map(TodoDTO::new).toList();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtoList)
                    .build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .error(error)
                    .build();

            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> readTodoList() {
        List<TodoDTO> dtoList = todoService.readTodoList();
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtoList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO request) {

        TodoEntity todoEntity = TodoDTO.toEntity(request);

        TodoDTO dto = todoService.updateTodo(todoEntity);

        List<TodoDTO> dtoList = List.of(dto);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtoList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO request) {
        Boolean delete = todoService.deleteTodo(request.getId());

        ResponseDTO<Boolean> response;
        if (delete) {
            response = ResponseDTO.<Boolean>builder()
                    .data(List.of(true))
                    .build();
        } else {
            response = ResponseDTO.<Boolean>builder()
                    .error("Todo is not exist")
                    .build();
        }

        return ResponseEntity.ok().body(response);
    }
}
