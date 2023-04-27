package com.elective.controller;

import com.elective.entity.User;
import com.elective.repositories.UserRepository;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;

    public UserController(CourseService courseService, UserService userService, TopicService topicService) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
    }

    @GetMapping("/register")
    public ModelAndView registerPage(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView addUser(@ModelAttribute("user")User user){
        userService.saveUser(user);
        return new ModelAndView("redirect:/courses");
    }

    @GetMapping("/teachers")
    public ModelAndView listTeachers(Model model){
        model.addAttribute("teachers", userService.getAllTeachers());
        return new ModelAndView("teachers");
    }

    @GetMapping("/teachers/addTeacher")
    public ModelAndView addCourseForm(Model model){

        User teacher = new User();
        model.addAttribute("teacher", teacher);

        return new ModelAndView("addTeacher");
    }

    @PostMapping("/teachers")
    public ModelAndView saveCourse(@ModelAttribute("course") User teacher){
        userService.addTeacher(teacher);
        return new ModelAndView("redirect:/teachers");
    }
}
