package com.elective.service.impl;

import com.elective.entity.Course;
import com.elective.repositories.CourseRepository;
import com.elective.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


    @Override
    public void addCourse(Course course) {
        courseRepository.save(course);
    }


    @Override
    public Course getCourseById(int id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}
