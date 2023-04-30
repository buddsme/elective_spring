package com.elective.service.impl;

import com.elective.entity.Course;
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

        List<User> teachers = new ArrayList<>();
        for (User user : users) {
            List<Role> roles = user.getRoles();
            for(Role role : roles){
                if (role.getRoleName().equals("TEACHER")) {
                    teachers.add(user);
                }
            }
        }
        return teachers;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveTeacher(User teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.getRoles().add(rolesRepository.getRolesByIdRole(2));
        userRepository.save(teacher);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(rolesRepository.getRolesByIdRole(3));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void blockUser(int id) {
        User currentUser = userRepository.findById(id);
        currentUser.setBlocked(true);
        userRepository.save(currentUser);
    }

    @Override
    public void unblockUser(int id) {
        User currentUser = userRepository.findById(id);
        currentUser.setBlocked(false);
        userRepository.save(currentUser);
    }

    @Override
    public void updateTeacher(int id, User newTeacher) {
        User currentTeacher = userRepository.findById(id);

        currentTeacher.setId(id);
        currentTeacher.setFirstName(newTeacher.getFirstName());
        currentTeacher.setSecondName(newTeacher.getSecondName());
        currentTeacher.setEmail(newTeacher.getEmail());
        currentTeacher.setPassword(passwordEncoder.encode(newTeacher.getPassword()));
        currentTeacher.setBlocked(newTeacher.isBlocked());

        userRepository.save(currentTeacher);
    }
}
