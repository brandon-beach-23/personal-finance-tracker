package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    Optional<User> findByUsername(String username);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
}
