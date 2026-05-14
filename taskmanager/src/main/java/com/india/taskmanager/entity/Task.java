package com.india.taskmanager.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
// import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public Task() {
}

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Priority getPriority(){
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority=priority;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status=status;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }
}
