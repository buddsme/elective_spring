package com.elective.service;

import com.elective.entity.Course;
import com.elective.entity.UserCoursesJournal;

import java.util.List;


public interface UserCoursesJournalService {
    List<Course> findUserAssignedCourses(int userId, List<Course> courses);
}
