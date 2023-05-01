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
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course")
    private int idCourse;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "date_start")
    private LocalDate dateStart;
    @Column(name = "date_end")
    private LocalDate dateEnd;
    @Column(name = "duration")
    private long duration;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @Column(name = "descriprion")
    private String description;

    public String getNameAndSurnameOfTeacher(User teacher){
        return teacher.getFirstName() + " " + teacher.getSecondName();
    }
}
