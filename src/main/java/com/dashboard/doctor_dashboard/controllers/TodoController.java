package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.services.todo.TodoService;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/todolist")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class TodoController {

    private TodoService todoService;
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
    public ResponseEntity<GenericMessage> addTodo(@Valid @RequestBody TodoListDto todolist  ) {
        log.info("TodoController:: addTodo");
        return todoService.addTodo(todolist);
    }

    /**
     * @param doctorId is used as path variable
     * @return All todos for the doctor with status code 200.
     * This endpoint is used for getting all todos of the doctor.
     */
    @GetMapping("doctor/{doctorId}")
        public ResponseEntity<GenericMessage> allTodoByDoctorId(@PathVariable("doctorId") Long doctorId) {
        log.info("TodoController:: allTodoByDoctorId");
        return todoService.getAllTodoByDoctorId(doctorId);
    }


    /**
     * @param id is used as path variable
     * @return Successfully delete after triggering delete api with status code 200.
     * This endpoint is used for deleting the todos of doctor.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteTodo(@PathVariable("id") Long id) {
        log.info("TodoController:: deleteTodo");
        return todoService.deleteTodoById(id);

    }



}
