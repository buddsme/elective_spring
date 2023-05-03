package com.elective.service.impl;

import com.elective.entity.Course;
import com.elective.entity.User;
import com.elective.entity.UserCoursesJournal;
import com.elective.repositories.CourseRepository;
import com.elective.repositories.UserCoursesJournalRepository;
import com.elective.repositories.UserRepository;
import com.elective.service.UserCoursesJournalService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserCoursesJournalServiceImpl implements UserCoursesJournalService {

    private final UserCoursesJournalRepository userCoursesJournalRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    public UserCoursesJournalServiceImpl(UserCoursesJournalRepository userCoursesJournalRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.userCoursesJournalRepository = userCoursesJournalRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public void saveUserCourse(int userId, int courseId) {
        User user = userRepository.findById(userId);
        Course course = courseRepository.findByIdCourse(courseId);

        UserCoursesJournal userCoursesJournal = new UserCoursesJournal();
        userCoursesJournal.setUser(user);
        userCoursesJournal.setCourse(course);
        userCoursesJournal.setGrade(0);
        userCoursesJournal.setJoinDate(LocalDate.now());

        userCoursesJournalRepository.save(userCoursesJournal);
    }

    @Override
    public List<Course> findUserAssignedCourses(int userId, List<Course> courses) {
        User user = userRepository.findById(userId);

        List<UserCoursesJournal> userCoursesJournals = userCoursesJournalRepository.findByUser(user);

        for (Course course : courses) {
            for (UserCoursesJournal journal : userCoursesJournals) {
                if (course.getIdCourse() == journal.getCourse().getIdCourse()) {
                    course.setAssignable(true);
                    break;
                }
            }
        }

        return courses;
    }
}
