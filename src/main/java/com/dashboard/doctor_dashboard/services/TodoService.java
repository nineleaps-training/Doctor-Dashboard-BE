package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * interface for todoservice layer.
 */
@Service
public interface TodoService {

    Todolist addTodo(TodoListDto todolist);


    List<Todolist> getAllTodoByDoctorId(Long doctorId);


    String deleteTodoById(Long id);
}