package com.anderson.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anderson.model.TodoDTO;
import com.anderson.repository.TodoRepository;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos") // Recuperação de todos os todos
    public ResponseEntity<?> getAllTodos() {
        List<TodoDTO> todos = todoRepository.findAll();
        if (todos.size() > 0) {
            return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No todos found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/todos") // Criação de um novo todo
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo) {
        try {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error creating todo: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todos/{id}") // Recuperação de um todo específico por ID
    public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id) {
        Optional<TodoDTO> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            return new ResponseEntity<TodoDTO>(todoOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Todo nothing was found for this ID.!", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}") // Alteração de um todo existente
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody TodoDTO todo) {
        Optional<TodoDTO> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            TodoDTO todoToSave = todoOptional.get();
            todoToSave.setCompleted(todo.isCompleted());
            todoToSave.setTodo(todo.getTodo() != null ? todo.getTodo() : todoToSave.getTodo());
            todoToSave.setDescription(
                    todo.getDescription() != null ? todo.getDescription() : todoToSave.getDescription());
            todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todoToSave);
            return new ResponseEntity<>(todoToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Todo nothing was found for this ID!" + id, HttpStatus.NOT_FOUND);
        }
    }

}
