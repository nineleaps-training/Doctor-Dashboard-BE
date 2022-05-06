package com.dashboard.doctor_dashboard.service.todo_service;

import com.dashboard.doctor_dashboard.entity.Todolist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    Todolist addTodo(Todolist todolist);

    Todolist getTodoById(Long id);

    List<Todolist> getAllTodoByDoctorId(Long doctorId);

    Todolist updateTodo(Long id, Todolist todolist);

    String deleteTodoById(Long id);
}