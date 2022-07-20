package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.services.todo_service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/todolist")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class TodoController {


    private TodoService todoService;
    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping()
    public ResponseEntity<GenericMessage> addTodo(@Valid @RequestBody TodoListDto todolist, HttpServletRequest request  ) {
        log.info("TodoController:: addTodo");
        return todoService.addTodo(todolist);
    }

    @GetMapping("doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllTodoByDoctorId(@PathVariable("doctorId") Long doctorId) {
        log.info("TodoController::getAllTodoByDoctorId");
        return todoService.getAllTodoByDoctorId(doctorId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericMessage> getTodoById(@PathVariable("id") Long id) {
        log.info("TodoController::getTodoById");
        return todoService.getTodoById(id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteTodo(@PathVariable("id") Long id) {
        log.info("TodoController::deleteTodo");
        return todoService.deleteTodoById(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericMessage> updateTodo( @Valid @RequestBody TodoListDto todolist,@PathVariable("id") Long id) {
        log.info("TodoController:: updateTodo");

        return todoService.updateTodo(id, todolist);
    }


}
