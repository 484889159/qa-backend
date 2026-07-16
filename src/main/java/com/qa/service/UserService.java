package com.qa.service;

import com.qa.entity.User;

public interface UserService {
    User login(String username, String password);
    User getUserById(Integer id);
    boolean updateUser(User user);
    String generateToken(User user);
    User getCurrentUser(String token);
    boolean register(User user);
}