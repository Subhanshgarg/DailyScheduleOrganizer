package com.Subhansh.DSO.DailyScheduleOrganiser.DTO;

import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
