package com.elective.service.mapper;

import com.elective.entity.User;
import com.elective.entity.dto.UserDTO;
import com.elective.entity.dto.UserWithImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserWithImageDTOMapper {
    UserWithImageDTOMapper INSTANCE = Mappers.getMapper(UserWithImageDTOMapper.class);
    UserWithImageDTO userToUserDTO(User user);
    User userDTOToUser(UserWithImageDTO userWithImageDTO);
}
