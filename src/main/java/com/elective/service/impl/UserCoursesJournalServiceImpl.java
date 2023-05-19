package com.elective.service.impl;

import com.elective.entity.Course;
import com.elective.entity.User;
import com.elective.entity.UserCoursesJournal;
import com.elective.entity.dto.UserCourseJournalDTO;
import com.elective.entity.dto.UserInitialsDTO;
import com.elective.repositories.CourseRepository;
import com.elective.repositories.UserCoursesJournalRepository;
import com.elective.repositories.UserRepository;
import com.elective.service.UserCoursesJournalService;
import com.elective.service.mapper.UserCourseJournalDTOMapper;
import com.elective.service.mapper.UserInitialsDTOMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        User user = userRepository.findUserById(userId);
        Course course = courseRepository.findByIdCourse(courseId);

        UserCoursesJournal userCoursesJournal = new UserCoursesJournal();
        userCoursesJournal.setUser(user);
        userCoursesJournal.setCourse(course);
        userCoursesJournal.setGrade(0);
        userCoursesJournal.setJoinDate(LocalDate.now());

        userCoursesJournalRepository.save(userCoursesJournal);
    }

    @Override
    public void findUserAssignedCourses(int userId, List<Course> courses) {
        User user = userRepository.findUserById(userId);

        List<UserCoursesJournal> userCoursesJournals = userCoursesJournalRepository.findAllByUser(user);

        for (Course course : courses) {
            for (UserCoursesJournal journal : userCoursesJournals) {
                if (course.getIdCourse() == journal.getCourse().getIdCourse()) {
                    course.setAssignable(true);
                    break;
                }
            }
        }
    }

    @Override
    public void countStudentsOnCourses(List<Course> courses) {

        for(Course course : courses){
            List<UserCoursesJournal> coursesJournal = userCoursesJournalRepository.findAllByCourse(course);
            course.setNumberOfStudents(coursesJournal.size());
        }
    }

    @Override
    public List<UserCourseJournalDTO> findAllJournalsByCourseName(String courseName) {
        List<UserCourseJournalDTO> userCourseJournalDTOS = new ArrayList<>();
        Course course = courseRepository.findByCourseName(courseName);
        List<UserCoursesJournal> userCoursesJournals = userCoursesJournalRepository.findAllByCourse(course);
        for(UserCoursesJournal userCoursesJournal : userCoursesJournals){
            UserInitialsDTO userInitialsDTO = UserInitialsDTOMapper.INSTANCE.userToUserInitialsDTO(userCoursesJournal.getUser());
            userCourseJournalDTOS.add(UserCourseJournalDTOMapper.INSTANCE.userCourseJournalToUserCourseJournalDTO(userCoursesJournal));
        }
        return userCourseJournalDTOS;
    }

    @Override
    public void updateUserGrade(int journalId, int newGrade) {
        UserCoursesJournal userCoursesJournal = userCoursesJournalRepository.findById(journalId);
        userCoursesJournal.setGrade(newGrade);
        userCoursesJournalRepository.save(userCoursesJournal);
    }
}
