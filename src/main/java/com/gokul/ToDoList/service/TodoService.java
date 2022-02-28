package com.gokul.ToDoList.service;

import com.gokul.ToDoList.entity.Todolist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    public Todolist addlist(Todolist todolist );
    public Todolist getlistById(Long id);
    public List <Todolist> getalllist();
    public Todolist updatelist(Long id, Todolist todolist);
    public void deletelistById(Long id);


}
