package com.elective.service;

import com.elective.entity.User;

import java.util.List;

public interface UserService{
    List<User> getAllTeachers();

    void addTeacher(User teacher);

    void saveUser(User user);
}
