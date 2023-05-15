package com.elective.service.impl;

import com.elective.entity.Image;
import com.elective.entity.User;
import com.elective.entity.dto.UserDTO;
import com.elective.entity.dto.UserWithImageDTO;
import com.elective.repositories.ImageRepository;
import com.elective.repositories.RolesRepository;
import com.elective.repositories.UserRepository;
import com.elective.service.UserService;
import com.elective.service.mapper.UserDTOMapper;
import com.elective.utils.ImageUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RolesRepository rolesRepository, ImageRepository imageRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> getAllUsersByRole(String roleToMatch) {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getRoles()
                        .contains(rolesRepository.getRoleByRoleName(roleToMatch)))
                .map(UserDTOMapper.INSTANCE::userToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findUserById(id);
    }

    @Override
    public UserDTO getUserDTOById(int id) {
        User user = userRepository.findUserById(id);
        return UserDTOMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public int saveUser(User user, int roleId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(rolesRepository.getRoleByIdRole(roleId));
        user.setRegisteredDate(LocalDate.now());
        return userRepository.save(user).getId();
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void blockUser(int id) {
        User currentUser = userRepository.findUserById(id);
        currentUser.setBlocked(true);
        userRepository.save(currentUser);
    }

    @Override
    public void unblockUser(int id) {
        User currentUser = userRepository.findUserById(id);
        currentUser.setBlocked(false);
        userRepository.save(currentUser);
    }

    @Override
    public void updateUser(int id, User newUser) {
        User userToUpdate = userRepository.findUserById(id);

        userToUpdate.setId(id);
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setSecondName(newUser.getSecondName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userToUpdate.setBlocked(newUser.isBlocked());

        userRepository.save(userToUpdate);
    }

    @Override
    public int getUserIdByEmail(String email) {
        return userRepository.findUserByEmail(email).getId();
    }

    @Override
    public List<UserWithImageDTO> setImagesForTeachers(List<UserDTO> teachers) {
        List<UserWithImageDTO> teachersWithImage = new ArrayList<>();
        for(UserDTO teacherDTO : teachers){
            UserWithImageDTO teacherWithImage = setImagesForUser(teacherDTO);
            teachersWithImage.add(teacherWithImage);
        }
        return teachersWithImage;
    }

    @Override
    public UserWithImageDTO setImagesForUser(UserDTO userDTO) {
        Image image;
        if(userDTO.getImage() == null || userDTO.getImage().getName().isEmpty()){
            image = imageRepository.getImageByName("user.png");
        }else{
            image = userDTO.getImage();
        }


        byte[] imageData = ImageUtil.decompressImage(image.getImage());
        String base64Image = Base64.getEncoder().encodeToString(imageData);

        return new UserWithImageDTO(userDTO.getId(), userDTO.getEmail(), userDTO.getFirstName(), userDTO.getSecondName(), base64Image, userDTO.getRegisteredDate(), userDTO.getLocationCity(), userDTO.getPhone());
    }

    @Override
    public void updateUserByUserDTO(UserWithImageDTO newUser) {
        User userToUpdate = userRepository.findUserById(newUser.getId());

        userToUpdate.setId(newUser.getId());
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setSecondName(newUser.getSecondName());
        userToUpdate.setPhone(newUser.getPhone());
        userToUpdate.setLocationCity(newUser.getLocationCity());

        userRepository.save(userToUpdate);
    }
}
