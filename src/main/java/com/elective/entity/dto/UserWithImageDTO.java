package com.elective.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserWithImageDTO {
    private int id;
    private String email;
    private String firstName;
    private String secondName;
    private String base64Image;
}
