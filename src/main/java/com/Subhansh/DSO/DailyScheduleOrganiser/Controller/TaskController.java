package com.Subhansh.DSO.DailyScheduleOrganiser.Controller;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.UpdatetaskRequestDTO;
import com.Subhansh.DSO.DailyScheduleOrganiser.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/taskManager/")
public class TaskController {
    private final TaskService taskService;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    // Endpoint to add a task
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping()
    public String addTask(@RequestBody TaskRequest taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication details for adding task: {}", authentication != null ? authentication.getName() : "Not authenticated");

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("User is not authenticated.");
            return "User is not authenticated.";
        }

        try {
            log.info("Received request to add task: {}", taskRequest);
            String response = taskService.addTask(taskRequest, taskRequest.getDate(), authentication.getName());
            log.info("Response from addTask: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("Error adding task: {}", taskRequest, ex);
            return "Error adding task.";
        }
    }
    // Endpoint to get all tasks for a specific date
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{date}")
    public List<TaskResponse> findAllTaskForParticularDate(@PathVariable("date") LocalDate date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Received request to find all tasks for date: {} for user: {}", date, authentication.getName());
        try {
            List<TaskResponse> tasks = taskService.findAllTaskForParticularDate(date,authentication.getName());
            log.info("Found {} tasks for date: {}", tasks.size(), date);
            return tasks;
        } catch (Exception ex) {
            log.error("Error fetching tasks for date: {}", date, ex);
            throw new RuntimeException("Error fetching tasks for the given date.");
        }
    }
    // Endpoint to update a task
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{date}/{id}")
    public TaskResponse updateTask(@PathVariable("date") LocalDate date, @PathVariable("id") int id,
                                   @RequestBody UpdatetaskRequestDTO updatetaskRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Received request to update task ID: {} for date: {}", id, date);

        try {
            TaskResponse updatedTask = taskService.updateTask(date, id, updatetaskRequestDTO,authentication.getName());
            log.info("Task updated successfully: {}", updatedTask);
            return updatedTask;
        } catch (Exception ex) {
            log.error("Error updating task ID: {} for date: {}", id, date, ex);
            throw new RuntimeException("Error updating task.");
        }
    }
    // Endpoint to delete a task
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{date}/{id}")
    public String deleteTask(@PathVariable("date") LocalDate date, @PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Received request to delete task ID: {} for date: {}", id, date);
        try {
            String response = taskService.deleteTask(date, id, authentication.getName());
            log.info("Response from deleteTask: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("Error deleting task ID: {} for date: {}", id, date, ex);
            return "Error deleting task.";
        }
    }
}
