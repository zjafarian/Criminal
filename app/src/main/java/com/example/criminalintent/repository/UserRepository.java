package com.example.criminalintent.repository;

import com.example.criminalintent.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    List<User> getUsers();
    User getUser(UUID userId);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    int getPositionUser(UUID userId);
}
