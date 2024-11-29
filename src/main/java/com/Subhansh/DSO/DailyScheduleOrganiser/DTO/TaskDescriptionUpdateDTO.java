package com.Subhansh.DSO.DailyScheduleOrganiser.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDescriptionUpdateDTO {

    private String description;

    // Getter and setter
}

