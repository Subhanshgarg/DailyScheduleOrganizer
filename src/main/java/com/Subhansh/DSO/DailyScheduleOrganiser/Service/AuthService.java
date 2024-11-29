package com.Subhansh.DSO.DailyScheduleOrganiser.Service;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.LoginRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.LoginResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.RegisterRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.Role;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.User;
import com.Subhansh.DSO.DailyScheduleOrganiser.Repository.UserRepository;
import com.Subhansh.DSO.DailyScheduleOrganiser.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request){
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Generate JWT token for authenticated user
            String token = jwtUtil.generateToken(request.getUsername());

            log.info("Login successful for username: {}", request.getUsername());
            return new LoginResponse(token);  // Return the token in the response
        } catch (Exception ex) {
            log.error("Login failed for username: {}", request.getUsername(), ex);
            return new LoginResponse("Invalid username or password");
        }
    }
    public String registerUser(RegisterRequest registerRequest){
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Username already exists: {}", registerRequest.getUsername());
            return "Username already exists!";
        }

        // Create a new user entity
        User user= User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(new HashSet<>(){{
                    add(Role.ROLE_USER);
         }}).build();
        // Save the user to the database
        userRepository.save(user);

        log.info("User registered successfully: {}", registerRequest.getUsername());
        return "User registered successfully!";
    }
}
