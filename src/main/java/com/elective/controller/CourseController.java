package com.elective.controller;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import com.elective.entity.User;
import com.elective.entity.dto.TopicDTO;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
import com.elective.service.impl.UserCoursesJournalServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
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
    public ModelAndView showMainPage(@RequestParam(required = false) String sort, @RequestParam(value = "topics", required = false) List<String> selectedTopics, Model model, Principal principal) {
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        List<Course> courses;

        boolean allFilter = false;
        if(selectedTopics != null) {
            for (String topic : selectedTopics) {
                if (topic.equals("All")) {
                    allFilter = true;
                    break;
                }
            }
        }

        if(selectedTopics == null || allFilter){
            courses = courseService.getAllCourses();
            List<String> emptyTopicsFilters = new ArrayList<>();
            model.addAttribute("selectedTopics", emptyTopicsFilters);
        }else{
            courses = courseService.filterByTopicsList(selectedTopics);
        }

        model.addAttribute("topics", topicService.getAllTopics());

        userCoursesJournalService.countStudentsOnCourses(courses);
        courseService.checkCoursesStatus(courses);

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
    public ModelAndView showTeacherCourses(@RequestParam(required = false) String sort, @RequestParam("teacherId") int id, Model model, Principal principal){
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);
        model.addAttribute("teacherId", id);

        User user = userService.getUserById(id);
        String userInitials = user.getFirstName() + " " + user.getSecondName();
        model.addAttribute("initials", userInitials);

        List<Course> courses = courseService.getAllCoursesByTeacherId(id);

        userCoursesJournalService.countStudentsOnCourses(courses);
        courseService.checkCoursesStatus(courses);
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
        return new ModelAndView("/client/teacher-courses");
    }

    @GetMapping("/main-page/course")
    public ModelAndView showCourseDetails(@RequestParam("courseId") int courseId, Model model, Principal principal){

        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        Course course = courseService.getCourseById(courseId);
        userCoursesJournalService.findUserAssignedCourse(userId, course);
        userCoursesJournalService.countStudentsOnCourse(course);
        model.addAttribute("course", course);

        TopicDTO topic = topicService.setImageToTopic(course.getTopic());
        model.addAttribute("topic", topic);



        return new ModelAndView("/client/course-details");
    }

    @GetMapping("/main-page/courses/topic")
    public ModelAndView showTopicCourses(@RequestParam("topicName") String topicName, Model model){
        Topic topic = topicService.getTopicByTopicName(topicName);
        List<Course> topicCourses = courseService.findAllByTopic(topic);
        model.addAttribute("courses", topicCourses);
        model.addAttribute("topicName", topicName);
        return new ModelAndView("/client/topic-courses");
    }
}
