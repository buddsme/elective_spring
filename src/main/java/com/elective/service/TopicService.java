package com.elective.service;

import com.elective.entity.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> getAllTopics();

    void addTopic(Topic topic);

    void deleteTopic(int id);

    Topic getTopicByTopicName(String name);
}
