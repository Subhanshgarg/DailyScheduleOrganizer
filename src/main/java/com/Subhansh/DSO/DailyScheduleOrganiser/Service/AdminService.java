package com.Subhansh.DSO.DailyScheduleOrganiser.Service;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.RegisterManagerRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.UserResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.Role;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.User;
import com.Subhansh.DSO.DailyScheduleOrganiser.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerManager(RegisterManagerRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Create a new manager user
        User manager = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(){{
                    add(Role.ROLE_MANAGER);
                }})
                .build();
        userRepository.save(manager);

        return "Manager registered successfully!";
    }

    public List<UserResponse> findAllUsersWithRoleUsers() {
        // Fetching users with 'ROLE_USER' from the repository
        List<User> users = userRepository.findAllUsersWithRole(Role.ROLE_USER);
        return users.stream()
                .map(this::mapToUserResponseObject)
                .collect(Collectors.toList());
    }

    public UserResponse mapToUserResponseObject(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
    public void assignManagerToUsers(Long managerId, List<Long> userIds) {
        // Fetch the manager
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager with ID " + managerId + " not found."));

        // Validate that the manager has ROLE_MANAGER
        if (!manager.getRoles().contains(Role.ROLE_MANAGER)) {
            throw new IllegalArgumentException("User with ID " + managerId + " is not a manager.");
        }

        // Fetch the users by IDs
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            // Validate that the user has ROLE_USER
            if (!user.getRoles().contains(Role.ROLE_USER)) {
                throw new IllegalArgumentException("User with ID " + user.getId() + " does not have the ROLE_USER role.");
            }

            // Assign the manager
            user.setManager(manager);
        }

        // Save all updated users
        userRepository.saveAll(users);
    }

    public List<UserResponse> findAllUsersWithRoleManagers() {
        List<User> users = userRepository.findAllUsersWithRole(Role.ROLE_MANAGER);
        return users.stream()
                .map(this::mapToUserResponseObject)
                .collect(Collectors.toList());
    }
}
