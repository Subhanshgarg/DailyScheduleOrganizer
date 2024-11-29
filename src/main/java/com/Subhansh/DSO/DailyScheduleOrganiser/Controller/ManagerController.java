package com.Subhansh.DSO.DailyScheduleOrganiser.Controller;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskDescriptionUpdateDTO;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.TaskResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.UserResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.Service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/manager")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    // Endpoint to find all users under the manager's supervision
    @GetMapping("/users")
    public List<UserResponse> getAllUsersUnderSupervision() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return managerService.findUsersUnderSupervision(authentication.getName());
    }

    // Endpoint to update a task description for a user under the manager's supervision
    @PutMapping("/tasks/{date}/{id}")
    public TaskResponse updateTaskDescription(
            @PathVariable("date") LocalDate date,
            @PathVariable("id") int id,
            @RequestBody TaskDescriptionUpdateDTO taskDescriptionUpdateDTO
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return managerService.updateTaskDescriptionForUser(date, id, taskDescriptionUpdateDTO, authentication.getName());
    }
}