package com.project.circuitbreakerandretrypattern.user.details.dto;

import java.time.LocalDateTime;

public class UserStatisticsDto {
    private int timeSpent;
    private LocalDateTime lastLogged;

    public UserStatisticsDto(int timeSpent, LocalDateTime lastLogged) {
        this.timeSpent = timeSpent;
        this.lastLogged = lastLogged;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public LocalDateTime getLastLogged() {
        return lastLogged;
    }
}
