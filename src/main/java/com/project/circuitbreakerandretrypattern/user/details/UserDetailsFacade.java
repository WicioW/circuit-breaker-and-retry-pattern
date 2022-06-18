package com.project.circuitbreakerandretrypattern.user.details;

import com.project.circuitbreakerandretrypattern.user.details.dto.UserDetailsDto;
import com.project.circuitbreakerandretrypattern.user.details.dto.UserDto;
import com.project.circuitbreakerandretrypattern.user.details.dto.UserStatisticsDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;


public class UserDetailsFacade {

  private final UserStatisticsService userStatisticsService;
  private final UserService userService;

  public UserDetailsFacade(UserStatisticsService userStatisticsService, UserService userService) {
    this.userStatisticsService = userStatisticsService;
    this.userService = userService;
  }

  public UserDetailsDto getUserDetails(int userId) {
    // TODO implement me
  }

}
