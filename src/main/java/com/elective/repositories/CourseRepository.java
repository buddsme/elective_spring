package com.elective.repositories;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findByIdCourse(int id);

    List<Course> findAllByTeacherId(int id);

    List<Course> findAllByTopic(Topic topic);
}
