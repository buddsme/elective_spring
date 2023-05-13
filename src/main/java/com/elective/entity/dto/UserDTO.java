package com.elective.entity.dto;

import com.elective.entity.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class UserDTO {
    @Id
    private int id;
    private String email;
    private String firstName;
    private String secondName;
    private boolean isBlocked;
    private Image image;
    private LocalDate registeredDate;
}
