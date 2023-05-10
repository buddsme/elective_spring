package com.elective.controller;

import com.elective.entity.Course;
import com.elective.entity.Image;
import com.elective.entity.User;
import com.elective.entity.dto.UserDTO;
import com.elective.entity.dto.UserWithImageDTO;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserCoursesJournalService;
import com.elective.service.UserService;
import com.elective.utils.ImageUtil;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class UserController {
    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;
    private final UserCoursesJournalService userCoursesJournalService;

    private final ResourceLoader resourceLoader;

    public UserController(CourseService courseService, UserService userService, TopicService topicService, UserCoursesJournalService userCoursesJournalService, ResourceLoader resourceLoader) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.userCoursesJournalService = userCoursesJournalService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/register")
    public ModelAndView registerPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView addUser(@ModelAttribute("user") User user){
        userService.saveUser(user, 3);
        return new ModelAndView("redirect:/courses");
    }

    @GetMapping("/main-page/teachers")
    public ModelAndView showAllTeachers(Model model){
        List<UserDTO> teachers = userService.getAllUsersByRole("TEACHER");
        List<UserWithImageDTO> teachersWithImage = new ArrayList<>();

        for(UserDTO teacherDTO : teachers){
            Image image = teacherDTO.getImage();
            byte[] imageData = ImageUtil.decompressImage(image.getImage());
            String base64Image = Base64.getEncoder().encodeToString(imageData);

            UserWithImageDTO teacherWithImage = new UserWithImageDTO(teacherDTO.getId(), teacherDTO.getEmail(), teacherDTO.getFirstName(), teacherDTO.getSecondName(), base64Image);
            teachersWithImage.add(teacherWithImage);
        }

        model.addAttribute("teachers", teachersWithImage);
        return new ModelAndView("client/teachers");
    }

    @GetMapping("/teacher/courses")
    public ModelAndView showTeacherCourses(@RequestParam("teacherId") int id, Model model){
        List<Course> courses = courseService.getAllCoursesByTeacherId(id);

        userCoursesJournalService.countStudentsOnCourses(courses);
        model.addAttribute("courses", courses);
        return new ModelAndView("/mainPage");
    }
}
