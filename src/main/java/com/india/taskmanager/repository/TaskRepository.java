package com.india.taskmanager.repository;
import com.india.taskmanager.entity.Priority;
import com.india.taskmanager.entity.Task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository extends JpaRepository<Task, Long> {
boolean existsByTitleIgnoreCase(String title);
List<Task> findByPriorityAndDueDate(Priority priority, LocalDate dueDate);
}
