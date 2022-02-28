package com.gokul.ToDoList.service;

import com.gokul.ToDoList.entity.Todolist;
import com.gokul.ToDoList.repository.TodoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoRespository todoRespository;
    @Override
    public Todolist addlist(Todolist todolist) {
        return todoRespository.save(todolist);
    }

    @Override
    public Todolist getlistById(Long id) {
        return todoRespository.findById(id).get();
    }

    @Override
    public List<Todolist> getalllist() {
        return todoRespository.findAll();
    }

    @Override
    public Todolist updatelist(Long id, Todolist todolist) {
       Todolist value = todoRespository.findById(id).get();
       value.setDescription(todolist.getDescription());
       value.setStatus(todolist.getStatus());
       return todoRespository.save(value);
    }

    @Override
    public void deletelistById(Long id) {
        todoRespository.deleteById(id);

    }
}
