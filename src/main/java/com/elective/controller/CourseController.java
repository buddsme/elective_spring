package com.elective.controller;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import com.elective.entity.User;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;


    public CourseController(CourseService courseService, UserService userService, TopicService topicService) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
    }

    @GetMapping("/courses")
    public ModelAndView listCourses(Model model){

        model.addAttribute("courses", courseService.getAllCourses());

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("courses");
    }

    @GetMapping("/courses/addCourse")
    public ModelAndView addCourseForm(Model model){

        Course course = new Course();
        model.addAttribute("course", course);

        List<User> teachers = userService.getAllTeachers();
        model.addAttribute("teachers", teachers);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("addCourse");
    }

    @PostMapping("/courses")
    public ModelAndView saveCourse(@ModelAttribute("course") Course course){

        long diffDate = countDaysOfCourse(course.getDateStart(), course.getDateEnd());

        course.setDuration(diffDate);

        courseService.addCourse(course);
        return new ModelAndView("redirect:/courses");
    }

    @GetMapping("/courses/edit/{id}")
    public ModelAndView editCourse(@PathVariable int id, Model model){
        model.addAttribute("course", courseService.getCourseById(id));
        List<User> teachers = userService.getAllTeachers();
        model.addAttribute("teachers", teachers);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return new ModelAndView("editCourse");
    }

    @PostMapping("/courses/{id}")
    public ModelAndView updateCourse(@PathVariable int id, @ModelAttribute("course") Course course){

        Course existingCourse = courseService.getCourseById(id);

        //existingCourse.setIdCourse(id);
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDateStart(course.getDateStart());
        existingCourse.setDateEnd(course.getDateEnd());


        long diffDays = countDaysOfCourse(course.getDateStart(), course.getDateEnd());

        existingCourse.setDuration(diffDays);
        existingCourse.setIdTeacher(course.getIdTeacher());
        existingCourse.setDescription(course.getDescription());

        courseService.updateCourse(existingCourse);

        return new ModelAndView("redirect:/courses");
    }

    @GetMapping("/courses/delete/{id}")
    public ModelAndView deleteCourse(@PathVariable int id){
        courseService.deleteCourse(id);
        return new ModelAndView("redirect:/courses");
    }

    private long countDaysOfCourse(LocalDate dateStart, LocalDate dateEnd){
        return ChronoUnit.DAYS.between(dateStart, dateEnd);
    }






}
