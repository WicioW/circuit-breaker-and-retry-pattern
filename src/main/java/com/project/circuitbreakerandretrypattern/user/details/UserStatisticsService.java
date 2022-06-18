package com.project.circuitbreakerandretrypattern.user.details;

import com.project.circuitbreakerandretrypattern.user.details.dto.UserStatisticsDto;

public interface UserStatisticsService {
    UserStatisticsDto getStatistics(int userId);
}
