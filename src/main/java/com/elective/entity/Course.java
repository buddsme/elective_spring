package com.elective.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_course")
    private int idCourse;

    @Column(name="course_name")
    private String courseName;

    @Column(name="date_start")
    private LocalDate dateStart;
    @Column(name="date_end")
    private LocalDate dateEnd;
    @Column(name="duration")
    private long duration;
    @Column(name="id_teacher")
    private int idTeacher;

    @Column(name="id_topic")
    private int idTopic;
    @Column(name="descriprion")
    private String description;
}
