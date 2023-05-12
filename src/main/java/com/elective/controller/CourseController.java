package com.elective.controller;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
import com.elective.service.impl.UserCoursesJournalServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;

    private final UserCoursesJournalServiceImpl userCoursesJournalService;

    public CourseController(CourseService courseService, UserService userService, TopicService topicService, UserCoursesJournalServiceImpl userCoursesJournalService) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.userCoursesJournalService = userCoursesJournalService;
    }

    @GetMapping("/main-page")
    public ModelAndView showMainPage(@RequestParam(required = false) String sort, Model model, Principal principal) {
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        List<Course> courses = courseService.getAllCourses();

        userCoursesJournalService.findUserAssignedCourses(userId, courses);
        userCoursesJournalService.countStudentsOnCourses(courses);

        if (sort != null) {
            switch (sort) {
                case "name(a-z)" -> courses.sort(Comparator.comparing(Course::getCourseName));
                case "name(z-a)" -> courses.sort(Comparator.comparing(Course::getCourseName, Collections.reverseOrder()));
                case "duration" -> courses.sort(Comparator.comparing(Course::getDuration));
                case "number-of-students" -> courses.sort(Comparator.comparingInt(Course::getNumberOfStudents));
                default -> {
                }
                //handle invalid sort parameter
            }
        }
        model.addAttribute("courses", courses);
        return new ModelAndView("/mainPage");
    }

    @PostMapping("/assign")
    public ModelAndView assignOnCourse(@RequestParam("userId") int userId, @RequestParam("courseId") int courseId) {
        userCoursesJournalService.saveUserCourse(userId, courseId);
        return new ModelAndView("redirect:/main-page");
    }

    @GetMapping("/main-page/teacher/courses")
    public ModelAndView showTeacherCourses(@RequestParam("teacherId") int id, Model model){
        List<Course> courses = courseService.getAllCoursesByTeacherId(id);

        userCoursesJournalService.countStudentsOnCourses(courses);
        model.addAttribute("courses", courses);
        return new ModelAndView("/mainPage");
    }

    @GetMapping("/main-page/topics")
    public ModelAndView showTopics(Model model){
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return new ModelAndView("client/topics");
    }

    @GetMapping("/main-page/topic/courses")
    public ModelAndView showTopicCourses(@RequestParam("topic") String topicName, Model model){
        Topic topic = topicService.getTopicByTopicName(topicName);
        List<Course> topicCourses = courseService.findAllByTopic(topic);
        model.addAttribute("courses", topicCourses);
        return new ModelAndView("/mainPage");
    }
}
