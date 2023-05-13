package com.elective.service.impl;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import com.elective.entity.User;
import com.elective.entity.UserCoursesJournal;
import com.elective.repositories.CourseRepository;
import com.elective.repositories.UserCoursesJournalRepository;
import com.elective.repositories.UserRepository;
import com.elective.service.CourseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserCoursesJournalRepository userCoursesJournalRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserCoursesJournalRepository userCoursesJournalRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userCoursesJournalRepository = userCoursesJournalRepository;
        this.userRepository = userRepository;
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
        existingCourse.setTeacher(course.getTeacher());
        existingCourse.setDescription(course.getDescription());
        courseRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> getAllCoursesByTeacherId(int id) {
        List<Course> courses = courseRepository.findAllByTeacherId(id);
        countProgressOfCourses(courses);
        return courseRepository.findAllByTeacherId(id);
    }

    @Override
    public List<Course> getAllCoursesByStudentId(int id) {
        User user = userRepository.findById(id);
        List<UserCoursesJournal> userCoursesJournals = userCoursesJournalRepository.findAllByUser(user);
        List<Course> courses = new ArrayList<>();
        for (UserCoursesJournal journal : userCoursesJournals) {
            courses.add(courseRepository.findByIdCourse(journal.getCourse().getIdCourse()));
        }
        countProgressOfCourses(courses);
        return courses;
    }

    @Override
    public List<Course> findAllByTopic(Topic topic) {
        return courseRepository.findAllByTopic(topic);
    }

    private void countProgressOfCourses(List<Course> courses) {
        for (Course course : courses) {
            if (LocalDate.now().isBefore(course.getDateStart())) {
                course.setProgress(0);
            } else {
                long daysOfCourse = countDaysOfCourse(course.getDateStart(), course.getDateEnd());
                long courseOnGoing = countDaysOfCourse(course.getDateStart(), LocalDate.now());
                double progressOfCourse =  ((double)courseOnGoing / (double)daysOfCourse) * 100;
                course.setProgress(progressOfCourse);
            }
        }
    }

    private long countDaysOfCourse(LocalDate dateStart, LocalDate dateEnd) {
        return ChronoUnit.DAYS.between(dateStart, dateEnd);
    }


}
