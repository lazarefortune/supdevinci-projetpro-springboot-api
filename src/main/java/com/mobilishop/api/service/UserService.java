package com.mobilishop.api.service;

import com.mobilishop.api.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> findAllUsers();
    User findUserById(Long id);
    User findUserByEmail(String email);
    User findByUserName(String userName);
    User updateUser(Long id, User user);
    void deleteUser(Long id);

    void addRoleToUser(String userEmail, String roleName);

    void removeRoleFromUser(String userEmail, String roleName);

    void init();
}
