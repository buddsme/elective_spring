package com.elective.service.mapper;

import com.elective.entity.UserCoursesJournal;
import com.elective.entity.dto.UserCourseJournalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserCourseJournalDTOMapper {
    UserCourseJournalDTOMapper INSTANCE = Mappers.getMapper(UserCourseJournalDTOMapper.class);

    @Mapping(source = "user", target = "userInitialsDTO")
    @Mapping(source = "id", target = "idJournal")
    UserCourseJournalDTO userCourseJournalToUserCourseJournalDTO(UserCoursesJournal userCoursesJournal);
}
