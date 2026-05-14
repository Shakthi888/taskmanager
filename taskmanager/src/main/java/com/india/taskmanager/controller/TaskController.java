package com.india.taskmanager.controller;
import com.india.taskmanager.entity.Task;
import com.india.taskmanager.repository.TaskRepository;
import com.india.taskmanager.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService service;
    @PostMapping
    public Task addTask(@Valid @RequestBody Task task){
        return service.saveTask(task);
    }
    @GetMapping
    public List<Task> getTasks() {
        return service.getAllTasks();
    }
    @GetMapping("/reminders")
    public List<Task> getReminders() {
    return service.getHighPriorityTasksDueToday();    
    }
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody Task task){
        return service.updateTask(id, task);
    }
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id){
        service.deleteTask(id);
        return "Deleted successfully";
    }
}
