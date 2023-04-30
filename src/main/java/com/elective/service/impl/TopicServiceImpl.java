package com.elective.service.impl;

import com.elective.entity.Topic;
import com.elective.repositories.TopicRepository;
import com.elective.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public void addTopic(Topic topic) {
        topicRepository.save(topic);
    }

    @Override
    public void deleteTopic(int id) {
        topicRepository.deleteById(id);
    }
}
