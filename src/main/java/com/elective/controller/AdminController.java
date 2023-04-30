package com.elective.controller;

import com.elective.entity.Course;
import com.elective.entity.Topic;
import com.elective.entity.User;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;
    private final TopicService topicService;

    public AdminController(UserService userService, CourseService courseService, TopicService topicService) {
        this.userService = userService;
        this.courseService = courseService;
        this.topicService = topicService;
    }

    @GetMapping
    public ModelAndView showAdminPage(){
        return new ModelAndView("administration/adminPage");
    }

    //teacher controller

    @GetMapping("/teachers")
    public ModelAndView listTeachers(Model model){
        model.addAttribute("teachers", userService.getAllTeachers());
        return new ModelAndView("administration/teachers/teachersInfo");
    }

    @GetMapping("/teachers/addTeacher")
    public ModelAndView addTeacherForm(Model model){

        User teacher = new User();
        model.addAttribute("teacher", teacher);

        return new ModelAndView("administration/teachers/addTeacher");
    }

    @PostMapping("/teachers/addTeacher")
    public ModelAndView saveTeacher(@ModelAttribute("teacher") User teacher){
        userService.saveTeacher(teacher);
        return new ModelAndView("redirect:/admin/teachers");
    }

    @GetMapping("/teachers/delete/{id}")
    public ModelAndView deleteTeacher(@PathVariable int id){
        userService.deleteUser(id);
        return new ModelAndView("redirect:/admin/teachers");
    }

    @GetMapping("/teachers/edit/{id}")
    public ModelAndView editTeacher(@PathVariable int id, Model model){
        model.addAttribute("teacher", userService.getUserById(id));
        return new ModelAndView("administration/teachers/editTeacher");
    }

    @PostMapping("/teachers/edit/{id}")
    public ModelAndView updateTeacher(@PathVariable int id, @ModelAttribute("teacher") User teacher){
        userService.updateTeacher(id,teacher);

        return new ModelAndView("redirect:/admin/teachers");
    }

    @PostMapping("setAccountStatus/{id}")
    public ModelAndView updateUserStatus(@PathVariable int id){
        if(!userService.getUserById(id).isBlocked()){
            userService.blockUser(id);
        }else{
            userService.unblockUser(id);
        }
        return new ModelAndView("redirect:/admin/teachers");
    }

    // Course controller
    @GetMapping("/courses")
    public ModelAndView listCourses(Model model){

        model.addAttribute("courses", courseService.getAllCourses());

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("administration/courses/coursesInfo");
    }

    @GetMapping("/courses/addCourse")
    public ModelAndView addCourseForm(Model model){

        Course course = new Course();
        model.addAttribute("course", course);

        List<User> teachers = userService.getAllTeachers();
        model.addAttribute("teachers", teachers);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("administration/courses/addCourse");
    }

    @PostMapping("/courses/addCourse")
    public ModelAndView saveCourse(@ModelAttribute("course") Course course){
        courseService.addCourse(course);
        return new ModelAndView("redirect:/admin/courses");
    }

    @GetMapping("/courses/edit/{id}")
    public ModelAndView editCourse(@PathVariable int id, Model model){
        model.addAttribute("course", courseService.getCourseById(id));
        List<User> teachers = userService.getAllTeachers();
        model.addAttribute("teachers", teachers);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return new ModelAndView("administration/courses/editCourse");
    }

    @PostMapping("/courses/edit/{id}")
    public ModelAndView updateCourse(@PathVariable int id, @ModelAttribute("course") Course course){
        courseService.updateCourse(id, course);

        return new ModelAndView("redirect:/admin/courses");
    }

    @GetMapping("/courses/delete/{id}")
    public ModelAndView deleteCourse(@PathVariable int id){
        courseService.deleteCourse(id);
        return new ModelAndView("redirect:/admin/courses");
    }
}
