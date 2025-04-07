package com.shinde.desicart.service;

import com.shinde.desicart.dto.SignupRequest;
import com.shinde.desicart.dto.UserDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.model.User;

public interface UserService {
    UserDto createUser(SignupRequest signupRequest) throws CustomException;
    UserDto getUser(String email);
    UserDto getUserById(Long userId);
    UserDto updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);

    // Add these new methods
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void saveUser(User user);
    User findByUsername(String username);
}
