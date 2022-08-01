package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.*;
import com.dashboard.doctor_dashboard.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;


import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/v1/todolist")
@Slf4j
public class TodoController {

    private final TodoService todoService;
    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    /**
     * @param todolist contains fields id,status and description
     * @return Successful message after creating todos for the doctor with status code 201.
     * This endpoint is used for adding new todos.
     */
    @PostMapping()
    public ResponseEntity<Todolist> addTodo(@Valid @RequestBody TodoListDto todolist  ) {
        log.info("TodoController:: addTodo");
        return new ResponseEntity<>(todoService.addTodo(todolist), HttpStatus.CREATED);
    }

    /**
     * @param doctorId is used as path variable
     * @return All todos for the doctor with status code 200.
     * This endpoint is used for getting all todos of the doctor.
     */
    @GetMapping("doctor/{doctorId}")
    public ResponseEntity<List<Todolist>> allTodoByDoctorId(@PathVariable("doctorId") Long doctorId) {
        log.info("TodoController:: allTodoByDoctorId");
        return new ResponseEntity<>(todoService.getAllTodoByDoctorId(doctorId),HttpStatus.OK);
    }


    /**
     * @param id is used as path variable
     * @return Successfully delete after triggering delete api with status code 200.
     * This endpoint is used for deleting the todos of doctor.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable("id") Long id) {
        log.info("TodoController:: deleteTodo");
        return new ResponseEntity<>(todoService.deleteTodoById(id),HttpStatus.NO_CONTENT);

    }



}