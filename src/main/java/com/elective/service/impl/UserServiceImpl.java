package com.elective.service.impl;

import com.elective.entity.Role;
import com.elective.entity.User;
import com.elective.repositories.RolesRepository;
import com.elective.repositories.UserRepository;
import com.elective.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<User> getAllUsersByRole(String roleToMatch) {
        List<User> users = userRepository.findAll();

        List<User> usersByRole = new ArrayList<>();
        for (User user : users) {
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getRoleName().equals(roleToMatch)) {
                    usersByRole.add(user);
                }
            }
        }
        return usersByRole;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user, int roleId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(rolesRepository.getRolesByIdRole(roleId));
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
    public void updateUser(int id, User newUser) {
        User userToUpdate = userRepository.findById(id);

        userToUpdate.setId(id);
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setSecondName(newUser.getSecondName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userToUpdate.setBlocked(newUser.isBlocked());

        userRepository.save(userToUpdate);
    }
}
