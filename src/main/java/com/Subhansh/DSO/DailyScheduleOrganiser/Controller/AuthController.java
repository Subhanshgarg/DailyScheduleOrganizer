package com.Subhansh.DSO.DailyScheduleOrganiser.Controller;

import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.LoginRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.LoginResponse;
import com.Subhansh.DSO.DailyScheduleOrganiser.DTO.RegisterRequest;
import com.Subhansh.DSO.DailyScheduleOrganiser.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    // Login Endpoint
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        return authService.login(request);
    }
    // Register Endpoint
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        log.info("Registration attempt for username: {}", registerRequest.getUsername());
        return authService.registerUser(registerRequest);
    }
}
