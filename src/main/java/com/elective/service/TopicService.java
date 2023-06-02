package com.elective.service;

import com.elective.entity.Topic;
import com.elective.entity.dto.TopicDTO;

import java.util.List;

public interface TopicService {
    List<Topic> getAllTopics();

    void addTopic(Topic topic);

    void deleteTopic(int id);

    Topic getTopicByTopicName(String name);

    TopicDTO setImageToTopic(Topic topic);
}
