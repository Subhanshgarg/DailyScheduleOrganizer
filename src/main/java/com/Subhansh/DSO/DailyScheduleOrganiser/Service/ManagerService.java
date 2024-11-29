package com.Subhansh.DSO.DailyScheduleOrganiser.Service;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskDescriptionUpdateDTO;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.UserResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.Task;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.User;
import com.Subhansh.DSO.DailyScheduleOrganiser.Exception.TaskNotFoundException;
import com.Subhansh.DSO.DailyScheduleOrganiser.Repository.TaskRepository;
import com.Subhansh.DSO.DailyScheduleOrganiser.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;


    // Find all users under a manager's supervision
    public List<UserResponse> findUsersUnderSupervision(String managerUsername) {
        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        // Find users who have the manager as their supervisor (assuming there's a field for that)
        List<User> usersUnderSupervision = userRepository.findByManager(manager); // Assuming a method in UserRepository

        return usersUnderSupervision.stream()
                .map(this::mapToUserResponseObject) // You would need a method to convert to UserResponse
                .toList();
    }

    // Update task description for a user under manager's supervision
    public TaskResponse updateTaskDescriptionForUser(LocalDate date, int taskId, TaskDescriptionUpdateDTO taskDescriptionUpdateDTO, String managerUsername) {
        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        // Find the task to be updated (only for users under the manager's supervision)
        List<Task> tasks = taskRepository.findByDateAndUser(date,manager); // Filter tasks by date and user
        Task taskToUpdate = tasks.stream()
                .filter(task -> task.getId() == taskId && task.getUser().getManager().equals(manager)) // Ensure task belongs to a user under manager's supervision
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task not found for the given ID"));

        // Update only the description
        taskToUpdate.setDescription(taskDescriptionUpdateDTO.getDescription());
        taskRepository.save(taskToUpdate);

        return taskService.mapToTaskResponse(taskToUpdate); // Assuming you have a method to map to TaskResponse
    }

    // Mapping methods (optional)
    private UserResponse mapToUserResponseObject(User user) {
        // Convert User entity to UserResponse DTO
        return new UserResponse(user.getId(), user.getUsername());
    }


}

