package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.Util.Constants;
import com.dashboard.doctor_dashboard.entities.Todolist;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.services.todo_service.TodoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void addTodo_Success() {
        Todolist todolist = new Todolist(1L,"Hi",true,null,null,null);
        GenericMessage messages  = new GenericMessage(Constants.SUCCESS,todolist);
        Mockito.when(todoService.addTodo(Mockito.any(Todolist.class))).thenReturn(new ResponseEntity<>(messages, HttpStatus.OK));
        ResponseEntity<GenericMessage> newTodo = todoController.addTodo(todolist);
        System.out.println(newTodo);
        assertEquals(messages.getData(),newTodo.getBody().getData());
    }


    @Test
    void getAllTodoByDoctorId_Success() {
        List<Todolist> todoList = new ArrayList<Todolist>();
        Todolist todolist1 = new Todolist(1L,"complete1",true,null,null,null);
        Todolist todolist2 = new Todolist(2L,"complete2",false,null,null,null);
        todoList.addAll(Arrays.asList(todolist1,todolist2));
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todoList);

        Mockito.when(todoService.getAllTodoByDoctorId(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));
        ResponseEntity<GenericMessage> newTodoList = todoController.getAllTodoByDoctorId(1L);

        System.out.println( newTodoList);
        assertEquals(todoList,newTodoList.getBody().getData());
    }

    @Test
    void getTodoById_Success() {
        Todolist todolist1 = new Todolist(1L,"Hi",false,null,null,null);
        GenericMessage messages  = new GenericMessage(Constants.SUCCESS,todolist1);

        Mockito.when(todoService.getTodoById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(messages, HttpStatus.OK));
        ResponseEntity<GenericMessage> newTodoList = todoController.getTodoById(1L);
        System.out.println( newTodoList);
        assertEquals(todolist1,newTodoList.getBody().getData());
    }

    @Test
    void deleteTodo_Success() {
        Todolist todolist1 = new Todolist(1L,"Hi",true,null,null,null);

        GenericMessage messages  = new GenericMessage(Constants.SUCCESS,"Deleted");

        Mockito.when(todoService.deleteTodoById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(messages, HttpStatus.OK));

        ResponseEntity<GenericMessage> newString1 = todoController.deleteTodo(1L);
        System.out.println(newString1);
        assertEquals("Deleted",newString1.getBody().getData());
    }


    @Test
    void updateTodo_Success() {
        Todolist todolist1 = new Todolist(1L,"Hi",false,null,null,null);
        GenericMessage messages  = new GenericMessage(Constants.SUCCESS,todolist1);

        Mockito.when(todoService.updateTodo(Mockito.any(Long.class),Mockito.any(Todolist.class))).thenReturn(new ResponseEntity<>(messages, HttpStatus.OK));

        ResponseEntity<GenericMessage> newTodoList1 = todoController.updateTodo(1L,todolist1);

        System.out.println(newTodoList1);
        assertEquals(todolist1,newTodoList1.getBody().getData());

    }
}