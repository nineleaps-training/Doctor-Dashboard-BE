package com.dashboard.doctor_dashboard.services.todo;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * interface for todoservice layer.
 */
@Service
public interface TodoService {

    ResponseEntity<GenericMessage> addTodo(TodoListDto todolist);


    ResponseEntity<GenericMessage> getAllTodoByDoctorId(Long doctorId);


    ResponseEntity<GenericMessage> deleteTodoById(Long id);
}
