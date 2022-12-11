package com.example.demo.service;

import com.example.demo.dto.TodoDTO;
import com.example.demo.entity.TodoEntity;
import com.example.demo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<TodoEntity> createTodo(final TodoEntity entity) {
        validate(entity);

        todoRepository.save(entity);
        log.info("Entity id : {} is saved", entity.getId());
        return todoRepository.findByUserId(entity.getUserId());
    }

    public List<TodoDTO> readTodoList() {
        List<TodoEntity> entityList = todoRepository.findAll();

        List<TodoDTO> dtoList = entityList.stream().map(TodoDTO::new).toList();

        return dtoList;
    }

    public TodoDTO updateTodo(final TodoEntity entity) {
        TodoEntity todo = todoRepository.findById(entity.getId()).orElseThrow(() -> new RuntimeException("Entity is not exist"));

        todo.setDone(entity.getDone());
        todo.setId(entity.getId());
        todo.setTitle(entity.getTitle());

        todoRepository.save(todo);

        return new TodoDTO(entity);
    }

    public Boolean deleteTodo(final String id) {
        if (todoRepository.findById(id).isPresent()) {
            todoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private void validate(final TodoEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
