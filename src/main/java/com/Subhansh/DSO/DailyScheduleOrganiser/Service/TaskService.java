package com.Subhansh.DSO.DailyScheduleOrganiser.Service;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.UpdatetaskRequestDTO;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.Task;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.User;
import com.Subhansh.DSO.DailyScheduleOrganiser.Exception.TaskNotFoundException;
import com.Subhansh.DSO.DailyScheduleOrganiser.Repository.TaskRepository;
import com.Subhansh.DSO.DailyScheduleOrganiser.Repository.UserRepository;
import com.Subhansh.DSO.DailyScheduleOrganiser.TaskFactory.TaskFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskFactory taskFactory;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Transactional
    public String addTask(TaskRequest taskRequest, LocalDate date, String username) {
        log.info("Request to add task: {}", taskRequest);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Task task = taskFactory.createTask(
                taskRequest.getDescription(),
                taskRequest.getStartTime(),
                taskRequest.getEndTime(),
                taskRequest.getPriority(),
                taskRequest.getDate(),
                user
        );

        List<Task> inDateTaskList = taskRepository.findByDate(date);

        if (inDateTaskList.isEmpty()) {
            taskRepository.save(task);
            log.info("Task saved successfully: {}", task);
            return "Task Saved Successfully";
        } else {
            for (Task inDate : inDateTaskList) {
                // Check if the time slot overlaps
                boolean isOverlapping = !(task.getEndTime().isBefore(inDate.getStartTime()) || task.getStartTime().isAfter(inDate.getEndTime()));

                if (isOverlapping) {
                    if (inDate.getPriority() >= task.getPriority()) {
                        log.warn("Cannot add task. A task with equal or higher priority already exists: {}", inDate);
                        return "Cannot add this task as another task already scheduled with equal or higher priority.";
                    } else {
                        log.info("Replacing task: {} with high priority task: {}", inDate, task);
                        taskRepository.delete(inDate);
                        taskRepository.save(task);
                        return "As this is a high priority task, it overrides the old task.";
                    }
                }
            }

            // If no overlapping tasks are found, save the new task
            taskRepository.save(task);
            log.info("Task saved successfully: {}", task);
            return "Task Saved Successfully";
        }
    }

    public List<TaskResponse> findAllTaskForParticularDate(LocalDate date, String username) {
        log.info("Fetching tasks for user: {} on date: {}", username, date);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch tasks associated with the user and the specific date
        List<Task> taskList = taskRepository.findByDateAndUser(date, user);
        return taskList.stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    public TaskResponse mapToTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .date(task.getDate())
                .priority(task.getPriority())
                .build();
    }

    public TaskResponse updateTask(LocalDate date, int id, UpdatetaskRequestDTO updatetaskRequestDTO, String username) {
        log.info("Updating task with ID: {} for date: {}", id, date);

        // Fetch the user based on the username (logged-in user)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch tasks for the provided date
        List<Task> taskDatedForProvidedDate = taskRepository.findByDate(date);

        if (taskDatedForProvidedDate.isEmpty()) {
            throw new TaskNotFoundException("No tasks found for the provided date: " + date);
        }

        // Find the task by ID and ensure the logged-in user is the one who created the task
        Task taskToBeUpdated = taskDatedForProvidedDate.stream()
                .filter(task -> task.getId()==id && task.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task not found for the given ID or user: " + id));

        // Update only the fields that are present in the DTO
        updatetaskRequestDTO.getDate().ifPresent(taskToBeUpdated::setDate);
        updatetaskRequestDTO.getDescription().ifPresent(taskToBeUpdated::setDescription);
        updatetaskRequestDTO.getPriority().ifPresent(taskToBeUpdated::setPriority);
        updatetaskRequestDTO.getStartTime().ifPresent(taskToBeUpdated::setStartTime);
        updatetaskRequestDTO.getEndTime().ifPresent(taskToBeUpdated::setEndTime);

        // Save the updated task in the repository
        taskRepository.save(taskToBeUpdated);
        log.info("Task updated successfully: {}", taskToBeUpdated);

        // Return the updated task as a response
        return mapToTaskResponse(taskToBeUpdated);
    }

    @Transactional
    public String deleteTask(LocalDate date, int id, String username) {
        log.info("Deleting task with ID: {} for date: {}", id, date);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Task> taskListWithGivenDate = taskRepository.findByDateAndUser(date, user);
        if (taskListWithGivenDate.isEmpty()) {
            throw new TaskNotFoundException("No tasks found for the provided date: " + date);
        } else {
            Task taskToBeDeleted = taskListWithGivenDate.stream()
                    .filter(task -> task.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new TaskNotFoundException("Task not found for the given ID: " + id));
            taskRepository.delete(taskToBeDeleted);
            log.info("Task deleted successfully: {}", taskToBeDeleted);
        }
        return "Task Deleted Successfully";
    }
}