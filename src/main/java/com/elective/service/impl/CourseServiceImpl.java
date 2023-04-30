package com.elective.service.impl;

import com.elective.entity.Course;
import com.elective.repositories.CourseRepository;
import com.elective.service.CourseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        long diffDate = countDaysOfCourse(course.getDateStart(), course.getDateEnd());
        course.setDuration(diffDate);

        courseRepository.save(course);
    }


    @Override
    public Course getCourseById(int id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public void updateCourse(int id, Course course) {
        Course existingCourse = courseRepository.findByIdCourse(id);

        existingCourse.setIdCourse(id);
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDateStart(course.getDateStart());
        existingCourse.setDateEnd(course.getDateEnd());

        long diffDays = countDaysOfCourse(course.getDateStart(), course.getDateEnd());

        existingCourse.setDuration(diffDays);
        existingCourse.setIdTeacher(course.getIdTeacher());
        existingCourse.setDescription(course.getDescription());
        courseRepository.save(existingCourse);
    }

    private long countDaysOfCourse(LocalDate dateStart, LocalDate dateEnd){
        return ChronoUnit.DAYS.between(dateStart, dateEnd);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}
