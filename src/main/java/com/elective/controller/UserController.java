package com.elective.controller;

import com.elective.entity.User;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
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
    public ModelAndView registerPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView addUser(@ModelAttribute("user") User user) {
        userService.saveUser(user, 3);
        return new ModelAndView("redirect:/courses");
    }


}
