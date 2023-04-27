package com.elective.service.impl;

import com.elective.entity.Role;
import com.elective.entity.User;
import com.elective.repositories.RolesRepository;
import com.elective.repositories.UserRepository;
import com.elective.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllTeachers() {
        List<User> users = userRepository.findAll();
        List<Role> roles = rolesRepository.findAll();
        List<User> teachers = new ArrayList<>();
        for(User user : users){
            for (Role f : roles) {
                if (f.equals(new Role("TEACHER"))) {
                    teachers.add(user);
                }
            }
        }
        return teachers;
    }

    @Override
    public void addTeacher(User teacher) {
        userRepository.save(teacher);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(rolesRepository.getRolesByIdRole(1));
        System.out.println(user);
        userRepository.save(user);
    }
}
