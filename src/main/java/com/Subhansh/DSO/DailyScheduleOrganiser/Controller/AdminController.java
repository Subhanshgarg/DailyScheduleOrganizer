package com.Subhansh.DSO.DailyScheduleOrganiser.Controller;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.RegisterManagerRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.UserResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register-manager")
    public String registerManager(@RequestBody RegisterManagerRequest request) {
        log.info("Admin received request to register manager: {}", request.getUsername());
        try {
            String response = adminService.registerManager(request);
            log.info("Manager registered successfully: {}", request.getUsername());
            return response;
        } catch (Exception e) {
            log.error("Error registering manager: {}", request.getUsername(), e);
            return "Error registering manager.";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> findAllUsersWithRoleUsers(){
        log.info("Admin wants to fetch list of all users");
        return adminService.findAllUsersWithRoleUsers();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/managers")
    public List<UserResponse> findAllUsersWithRoleManagers(){
        log.info("Admin wants to fetch list of all users");
        return adminService.findAllUsersWithRoleManagers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/assign-manager/{id}")
    public ResponseEntity<String> assignManagerToUsers(
            @PathVariable("id") Long managerId,
            @RequestBody List<Long> userIds) {
        try {
            adminService.assignManagerToUsers(managerId, userIds);
            return ResponseEntity.ok("Manager assigned successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
