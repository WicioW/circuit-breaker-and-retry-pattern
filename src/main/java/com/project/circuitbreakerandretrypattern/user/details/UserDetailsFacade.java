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
    return new UserDetailsDto(getUserDtoWithRetry(userId), getUserStatisticsDtoWithCBAndRetry(userId));
  }

  private UserDto getUserDtoWithRetry(int userId) {
    Supplier<UserDto> userDtoSupplier = () -> userService.getUser(userId);
    return Decorators.ofSupplier(userDtoSupplier).withRetry(getUserRetryPattern()).get();
  }


  private UserStatisticsDto getUserStatisticsDtoWithCBAndRetry(int userId) {
    CircuitBreakerConfig userStatisticsCircuitBreakerConfig =
            CircuitBreakerConfig.custom()
                    .failureRateThreshold(80)
                    .ringBufferSizeInClosedState(5)
                    .waitDurationInOpenState(Duration.ofSeconds(3))
                    .ignoreException(a -> a instanceof IllegalArgumentException)
                    .build();

    CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(userStatisticsCircuitBreakerConfig);
    CircuitBreaker circuitBreaker = registry.circuitBreaker("userStatisticsCB");
    Supplier<UserStatisticsDto> userStatisticsDtoSupplier =
            () -> userStatisticsService.getStatistics(userId);

    return
            Decorators.ofSupplier(userStatisticsDtoSupplier)
                    .withCircuitBreaker(circuitBreaker)
                    .withRetry(getUserStatisticsRetryPattern())
                    .withFallback(
                            List.of(CallNotPermittedException.class), throwable -> defaultUserStatistics()).get();
  }

  private Retry getUserRetryPattern() {
    RetryConfig config =
            RetryConfig.custom()
                    .maxAttempts(2) // 1 attempt, wait 1000 milliseconds, 2nd attempt
                    .waitDuration(Duration.ofMillis(1000))
                    .ignoreExceptions(IllegalArgumentException.class)
                    .build();
    RetryRegistry retryRegistry = RetryRegistry.of(config);
    return retryRegistry.retry("2attempts1000millis", config);
  }

  private Retry getUserStatisticsRetryPattern() {
    RetryConfig config =
            RetryConfig.custom()
                    .maxAttempts(2) // 1 attempt, wait 200 milliseconds, 2nd attempt
                    .waitDuration(Duration.ofMillis(200))
                    .ignoreExceptions(IllegalArgumentException.class)
                    .build();
    RetryRegistry retryRegistry = RetryRegistry.of(config);
    return retryRegistry.retry("2attempts200millis", config);
  }

  private UserStatisticsDto defaultUserStatistics() {
    return new UserStatisticsDto(0, null);
  }
}
