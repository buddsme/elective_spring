package com.elective.service.mapper;

import com.elective.entity.Topic;
import com.elective.entity.dto.TopicDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicDTOMapper {

    TopicDTOMapper INSTANCE = Mappers.getMapper(TopicDTOMapper.class);

    TopicDTO userToUserDTO(Topic topic);
    Topic userDTOToUser(TopicDTO topicDTO);
}
