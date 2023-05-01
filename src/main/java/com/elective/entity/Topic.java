package com.elective.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topic")
    private int idTopic;

    @Column(name = "topic_name")
    private String topicName;

    @OneToMany(mappedBy = "topic")
    private List<Course> courses;
}



