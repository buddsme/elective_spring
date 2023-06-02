package com.elective.entity.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopicDTO {

    @Id
    private int idTopic;
    private String topicName;
    private String base64Image;
}
