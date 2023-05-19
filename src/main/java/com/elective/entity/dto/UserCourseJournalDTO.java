package com.elective.entity.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCourseJournalDTO {
    @Id
    private int idJournal;
    private UserInitialsDTO userInitialsDTO;
    private int grade;
    private LocalDate joinDate;
}
