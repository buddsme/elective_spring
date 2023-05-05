package com.elective.controller;

import com.elective.entity.Course;
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
}
