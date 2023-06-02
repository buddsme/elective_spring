package com.elective.service;

import com.elective.entity.Course;
import com.elective.entity.dto.UserCourseJournalDTO;

import java.util.List;


public interface UserCoursesJournalService {

    void findUserAssignedCourse(int userId, Course course);

    void countStudentsOnCourses(List<Course> course);

    void countStudentsOnCourse(Course course);

    List<UserCourseJournalDTO> findAllJournalsByCourseName(String courseName);

    void updateUserGrade(int journalId, int newGrade);

    void deleteUserOnCourse(int courseId, int userId);
}
