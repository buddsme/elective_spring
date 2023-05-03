package com.elective.service;

import com.elective.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsersByRole(String roleToMatch);

    User getUserById(int id);

    void updateUser(int id, User newUser);

    void saveUser(User user, int role);

    void deleteUser(int id);

    void blockUser(int id);

    void unblockUser(int id);

    int getUserIdByEmail(String email);
}
