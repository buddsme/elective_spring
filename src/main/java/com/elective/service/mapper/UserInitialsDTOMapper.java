package com.elective.service.mapper;

import com.elective.entity.User;
import com.elective.entity.dto.UserInitialsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface UserInitialsDTOMapper {
    UserInitialsDTOMapper INSTANCE = Mappers.getMapper(UserInitialsDTOMapper.class);

    UserInitialsDTO userToUserInitialsDTO(User user);
}