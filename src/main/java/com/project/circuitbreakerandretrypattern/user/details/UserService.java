package com.project.circuitbreakerandretrypattern.user.details;

import com.project.circuitbreakerandretrypattern.user.details.dto.UserDto;

public interface UserService {
    UserDto getUser(int userId);
}
