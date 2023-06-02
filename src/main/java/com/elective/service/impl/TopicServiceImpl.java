package com.elective.service.impl;

import com.elective.entity.Image;
import com.elective.entity.Topic;
import com.elective.entity.dto.TopicDTO;
import com.elective.repositories.ImageRepository;
import com.elective.repositories.TopicRepository;
import com.elective.service.TopicService;
import com.elective.utils.ImageUtil;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final ImageRepository imageRepository;

    public TopicServiceImpl(TopicRepository topicRepository, ImageRepository imageRepository) {
        this.topicRepository = topicRepository;
        this.imageRepository = imageRepository;
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

    @Override
    public Topic getTopicByTopicName(String name) {
        return topicRepository.getTopicByTopicName(name);
    }

    @Override
    public TopicDTO setImageToTopic(Topic topic) {
        Image image;
        if(topic.getImage() == null || topic.getImage().getName().isEmpty()){
            image = imageRepository.getImageByName("no-image.png");
        }else{
            image = topic.getImage();
        }


        byte[] imageData = ImageUtil.decompressImage(image.getImage());
        String base64Image = Base64.getEncoder().encodeToString(imageData);

        return new TopicDTO(topic.getIdTopic(), topic.getTopicName(), base64Image);
    }
}
