package com.gokul.ToDoList.repository;

import com.gokul.ToDoList.entity.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRespository extends JpaRepository<Todolist,Long> {

}
