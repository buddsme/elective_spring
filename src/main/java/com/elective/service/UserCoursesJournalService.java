package com.elective.service;

import com.elective.entity.Course;

import java.util.List;


public interface UserCoursesJournalService {
    void findUserAssignedCourses(int userId, List<Course> courses);

    void countStudentsOnCourses(List<Course> course);
}
