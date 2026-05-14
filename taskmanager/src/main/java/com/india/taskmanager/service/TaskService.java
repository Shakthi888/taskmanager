package com.india.taskmanager.service;
import com.india.taskmanager.entity.Task;
import com.india.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDate;
import com.india.taskmanager.entity.Priority;

import com.india.taskmanager.exception.DuplicateResourceException;
import com.india.taskmanager.exception.ResourceNotFoundException;
import java.util.List;
@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public Task saveTask(Task task){
        if(repository.existsByTitleIgnoreCase(task.getTitle()))
        {
            throw new DuplicateResourceException("Task with this title already exists");
        }
        return repository.save(task);
    }

    public List<Task> getAllTasks(){
        return repository.findAll();
    }

    public void deleteTask(Long id){
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        repository.delete(task);
    }
    public Task updateTask(Long id, Task newTask) {
    Task task = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
            
            // for name
if (repository.existsByTitleIgnoreCase(newTask.getTitle())
                && !task.getTitle().equalsIgnoreCase(newTask.getTitle())) {

            throw new DuplicateResourceException("Task with this title already exists");
        }
 
    task.setTitle(newTask.getTitle());
    task.setDescription(newTask.getDescription());
    task.setPriority(newTask.getPriority());
    task.setStatus(newTask.getStatus());
    task.setDueDate(newTask.getDueDate());
 
    return repository.save(task);
}
public List<Task> getHighPriorityTasksDueToday() {
    return repository.findByPriorityAndDueDate(Priority.HIGH, LocalDate.now());
}


@Scheduled(cron = "0 * * * * *") // runs every minute
public void remindHighPriorityTasks() {

    LocalDate today = LocalDate.now();

    List<Task> tasks = repository.findByPriorityAndDueDate(Priority.HIGH, today);

    for (Task task : tasks) {
        System.out.println("🔔 Reminder: High priority task due today -> " + task.getTitle());
    }
}

}