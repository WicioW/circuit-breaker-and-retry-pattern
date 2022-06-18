package com.project.circuitbreakerandretrypattern.user.details.dto;

public class UserDetailsDto {
    private UserDto user;
    private UserStatisticsDto statistic;

    public UserDetailsDto(UserDto user, UserStatisticsDto statistic) {
        this.user = user;
        this.statistic = statistic;
    }

    public UserDto getUser() {
        return user;
    }

    public UserStatisticsDto getStatistic() {
        return statistic;
    }
}
