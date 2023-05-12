package com.elective.repositories;

import com.elective.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Topic getTopicByTopicName(String topic);
}
