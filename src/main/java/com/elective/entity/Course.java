package com.elective.entity;

import com.elective.entity.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "course",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoursesJournal> studentCourses = new ArrayList<>();
    @Transient
    private boolean isAssignable;
    @Transient
    private int numberOfStudents;
    @Transient
    private double progress;
    private CourseStatus courseStatus;
}
