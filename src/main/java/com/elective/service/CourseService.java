package com.elective.service;

import com.elective.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();

    List<Course> getAllCoursesByTeacherId(int id);

    void addCourse(Course course);

    Course getCourseById(int id);

    void updateCourse(int id, Course course);

    void deleteCourse(int id);
}
