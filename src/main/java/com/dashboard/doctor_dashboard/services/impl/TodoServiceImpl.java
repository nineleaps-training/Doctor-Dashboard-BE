package com.dashboard.doctor_dashboard.services.impl;

import com.dashboard.doctor_dashboard.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.Todolist;
import com.dashboard.doctor_dashboard.repository.TodoRepository;
import com.dashboard.doctor_dashboard.services.TodoService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * implementation of TodoService interface
 */
@Service
@Slf4j
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private ModelMapper mapper;
    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository,ModelMapper mapper) {
        this.todoRepository = todoRepository;
        this.mapper=mapper;
    }

    /**
     * This function of service is for adding todos/task  for doctor.
     *
     * @param todolist which contains fields description,status and doctor details
     * @return ResponseEntity<GenericMessage> with status code 201.
     */
    @Override
    public Todolist addTodo(TodoListDto todolist) {
        log.info("inside: TodoServiceImpl::addTodo");

        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.save(mapper.map(todolist,Todolist.class)));
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::addTodo");

        return todoRepository.save(mapper.map(todolist,Todolist.class));
    }

    /**
     * This function of service is for getting all todos of the doctor by id
     *
     * @param doctorId
     * @return ResponseEntity<GenericMessage> with status code 200 and list of todos.
     */
    @Override
    public List<Todolist> getAllTodoByDoctorId(Long doctorId) {
        log.info("inside: TodoServiceImpl::getAllTodoByDoctorId");

        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.findByDoctorId(doctorId));
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::getAllTodoByDoctorId");

        return todoRepository.findByDoctorId(doctorId);
    }


    /**
     * This function of service is for deleting todos/task by id
     *
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 204 and message successfully deleted.
     */
    @Override
    public String deleteTodoById(Long id) {
        log.info("inside: TodoServiceImpl::deleteTodoById");

        var genericMessage = new GenericMessage();

        todoRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::deleteTodoById");

        return "successfully deleted";

    }
}