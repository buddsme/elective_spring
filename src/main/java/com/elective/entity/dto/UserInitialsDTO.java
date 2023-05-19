package com.elective.entity.dto;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInitialsDTO {
    @Id
    private int id;
    private String firstName;
    private String secondName;
}
