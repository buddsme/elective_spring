package com.elective.service;

import com.elective.entity.User;
import com.elective.entity.dto.UserDTO;
import com.elective.entity.dto.UserWithImageDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsersByRole(String roleToMatch);

    User getUserById(int id);

    UserDTO getUserDTOById(int id);

    void updateUser(int id, User newUser);

    int saveUser(User user, int role);

    void deleteUser(int id);

    void blockUser(int id);

    void unblockUser(int id);

    int getUserIdByEmail(String email);

    List<UserWithImageDTO> setImagesForTeachers(List<UserDTO> teachers);

    UserWithImageDTO setImagesForUser(UserDTO userDTO);
}
