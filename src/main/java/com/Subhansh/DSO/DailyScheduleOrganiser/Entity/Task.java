package com.Subhansh.DSO.DailyScheduleOrganiser.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String description;
    @NonNull
    private LocalDate date;
    @NonNull
    private LocalTime startTime;

    @NonNull
    private LocalTime endTime;

    private int priority;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to User table
    private User user;

    public Task(String description, LocalDate date, LocalTime startTime, LocalTime endTime, int priority, User user) {
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.user=user;
    }

}