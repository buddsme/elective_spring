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
}
