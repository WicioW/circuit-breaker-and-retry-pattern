# Introduction

You are going to develop a circuit breaker, a fallback and retry pattern in microserivce architecture.

There are two services, that is `UserService` and `UserStatisticsService`, which simulate external microservices.

- User service is a critical service, which returns the first and the last name of a user.
- User statistics service is a service, which returns less important data (time spent in the application and last logged in date),
  and in case of a failure , it should fall back to the default response.

# Problem statement
You should implement a resilient mechanism in `UserDetailsFacade` that will combine the responses from the above services
and return them to the caller with the following requirements:

For communication with `UserService`:
- short-circuit is not allowed, therefore you cannot use circuit breaker pattern
- you have to implement a retry pattern (1 attempt and in case of a failure, only 1 additional retry attempt after 1000ms)
- there is no fallback response, in case of a failure, you have to return it to the caller.

For communication with `UserStatisticsService`:
- you have to implement a circuit breaker and a fallback pattern:
    - after minimum 5 requests and 80% of a failing requests, you should short-circuit(change to OPEN state)
      and no longer communicate with this service
    - when the circuit is OPEN, you should immediately return the default fallback response: null last logged in date and 0 seconds for the time spent in the application
    - when the circuit is OPEN, it has to wait for 3s upon being switched to HALF-OPEN
- you have to implement a retry pattern(1 attempt and in case of a failure, only 1 additional retry attempt after 200ms)
    - if the first attempt is unsuccessful and the retry attempt is successful, it should NOT increase the failure count for the circuit breaker.

If any of those services returns an `IllegalArgumentException`(which can be treated as a developer mistake), then:
- you should immediately return this exception to the caller,
- it should not affect the circuit breaker ( it should NOT increase the failure count),
- there should not be any retry attempts to communicate with the service once again (as it would return exactly the same response!)

Good luck!

# Hints
- You don't have to support the Time Limiter pattern. You can assume that the user service and the statistics service respond immediately
  and don't timeout.
- You can implement this task however you like, but using resilience4j-circuitbreaker and resilience4j-retry libraries are recommended.
