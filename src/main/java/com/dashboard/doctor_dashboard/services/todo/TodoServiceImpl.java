package com.dashboard.doctor_dashboard.services.todo;

import com.dashboard.doctor_dashboard.entities.Todolist;
import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.TodoRepository;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public ResponseEntity<GenericMessage> addTodo(TodoListDto todolist) {
        log.info("inside: TodoServiceImpl::addTodo");

        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.save(mapper.map(todolist,Todolist.class)));
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::addTodo");

        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<GenericMessage> getAllTodoByDoctorId(Long doctorId) {
        log.info("inside: TodoServiceImpl::getAllTodoByDoctorId");

        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.findByDoctorId(doctorId));
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::getAllTodoByDoctorId");

        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }



    @Override
    public ResponseEntity<GenericMessage> deleteTodoById(Long id) {
        log.info("inside: TodoServiceImpl::deleteTodoById");

        var genericMessage = new GenericMessage();

        todoRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::deleteTodoById");

        return new ResponseEntity<>(genericMessage, HttpStatus.OK);

    }
}
