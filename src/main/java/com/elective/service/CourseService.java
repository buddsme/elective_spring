package com.elective.service;

import com.elective.entity.Course;
import com.elective.entity.Topic;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();

    List<Course> getAllCoursesByTeacherId(int id);

    List<Course> findAllByTopic(Topic topic);

    void addCourse(Course course);

    Course getCourseById(int id);

    void updateCourse(int id, Course course);

    void deleteCourse(int id);

    List<Course> getAllCoursesByStudentId(int id);

    List<Course> checkCoursesStatus(List<Course> courses);

}
