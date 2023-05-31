package com.elective.service.impl;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import com.elective.entity.User;
import com.elective.entity.UserCoursesJournal;
import com.elective.entity.enums.CourseStatus;
import com.elective.repositories.CourseRepository;
import com.elective.repositories.TopicRepository;
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

    private final TopicRepository topicRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserCoursesJournalRepository userCoursesJournalRepository, UserRepository userRepository, TopicRepository topicRepository) {
        this.courseRepository = courseRepository;
        this.userCoursesJournalRepository = userCoursesJournalRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
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
        User user = userRepository.findUserById(id);
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

    @Override
    public List<Course> checkCoursesStatus(List<Course> courses) {
        for(Course course : courses){
            if(course.getDateEnd().plusDays(5).isBefore(LocalDate.now())){
                courseRepository.deleteById(course.getIdCourse());
                courses.remove(course);
                break;
            }
            else if(course.getDateStart().isBefore(LocalDate.now()) && course.getDateEnd().isAfter(LocalDate.now())){
                course.setCourseStatus(CourseStatus.ONGOING);
            } else if (course.getDateStart().equals(LocalDate.now()) || course.getDateEnd().equals(LocalDate.now())) {
                course.setCourseStatus(CourseStatus.ONGOING);
            } else if (course.getDateStart().isAfter(LocalDate.now())) {
                course.setCourseStatus(CourseStatus.NOT_BEGUN);
            } else if(course.getDateEnd().isBefore(LocalDate.now())){
                course.setCourseStatus(CourseStatus.FINISHED);
            }
        }
        List<Course> updatedCourses = courseRepository.saveAll(courses);
        return updatedCourses;
    }

    @Override
    public String getCourseNameByJournalId(int journalId) {
        UserCoursesJournal userCoursesJournal = userCoursesJournalRepository.findById(journalId);
        Course course = userCoursesJournal.getCourse();
        return course.getCourseName();
    }

    @Override
    public String getGradeOfCourse(String courseName, String userId) {
        Course course = courseRepository.findByCourseName(courseName);
        User user = userRepository.findUserById(Integer.parseInt(userId));
        UserCoursesJournal userCoursesJournal = userCoursesJournalRepository.findByCourseAndUser(course, user);
        return String.valueOf(userCoursesJournal.getGrade());
    }

    @Override
    public List<Course> filterByTopicsList(List<String> selectedTopics) {
        List<Course> courses = new ArrayList<>();
        for(String topic : selectedTopics){
            List<Course> topicCourses = courseRepository.findAllByTopic(topicRepository.getTopicByTopicName(topic));
            if (topicCourses != null) {
                courses.addAll(topicCourses);
            }
        }
        return courses;
    }
}
