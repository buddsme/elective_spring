package com.elective.service;

import com.elective.entity.User;

import java.util.List;

public interface UserService{
    List<User> getAllTeachers();

    User getUserById(int id);

    void saveTeacher(User teacher);

    void updateTeacher(int id, User teacher);

    void saveUser(User user);

    void deleteUser(int id);

    void blockUser(int id);

    void unblockUser(int id);
}
