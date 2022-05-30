package com.mobilishop.api.service;

import com.mobilishop.api.model.Card;
import com.mobilishop.api.model.User;

import java.util.List;

public interface UserService {
  String createUser(User user);

  List<User> findAllUsers();

  User findUserById(Long id);

  User findUserByEmail(String email);

  User findByUserName(String userName);

  User updateUser(Long id, User user);

  void deleteUser(Long id);

  String confirmToken(String token);

  void addRoleToUser(String userEmail, String roleName);

  void removeRoleFromUser(String userEmail, String roleName);

  Card addCardToUser(Long userId, Card card);

  void init();
}
