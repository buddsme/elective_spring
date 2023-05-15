package com.elective.entity.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserWithImageDTO {
    @Id
    private int id;
    private String email;
    private String firstName;
    private String secondName;
    private String base64Image;
    private LocalDate registeredDate;
    private String locationCity;
    private String phone;
}
