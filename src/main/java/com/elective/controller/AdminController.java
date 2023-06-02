package com.elective.controller;

import com.elective.entity.*;
import com.elective.entity.dto.UserDTO;
import com.elective.repositories.ImageRepository;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserService;
import com.elective.utils.ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;
    private final TopicService topicService;

    private final ImageRepository imageRepository;

    public AdminController(UserService userService, CourseService courseService, TopicService topicService, ImageRepository imageRepository) {
        this.userService = userService;
        this.courseService = courseService;
        this.topicService = topicService;
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public ModelAndView showAdminPage() {
        return new ModelAndView("administration/adminPage");
    }

    //teacher controller

    @GetMapping("/users")
    public ModelAndView listUsers(@RequestParam("role") String role, Model model) {
        if (role.equals("teacher")) {
            model.addAttribute("users", userService.getAllUsersByRole("TEACHER"));
            model.addAttribute("role", role);
        } else if (role.equals("student")) {
            model.addAttribute("users", userService.getAllUsersByRole("STUDENT"));
            model.addAttribute("role", role);
        }
        return new ModelAndView("administration/users/usersInfo");
    }

    @GetMapping("/users/addUser")
    public ModelAndView addUserForm(@RequestParam("role") String role) {

        ModelAndView mav = new ModelAndView("administration/users/addUser");
        if (role.equals("teacher")) {
            User user = new User();
            mav.addObject("user", user);
            mav.addObject("role", role);
        } else if (role.equals("student")) {
            User user = new User();
            mav.addObject("user", user);
            mav.addObject("role", role);
        }
        return mav;
    }

    @PostMapping("/users/addUser")
    public ModelAndView saveUser(@RequestParam("role") String role, @RequestParam(value = "image", required = false) MultipartFile file, @ModelAttribute("user") User user,
                                 Errors validationErrors) throws IOException {

        validationErrors.rejectValue("file", "rejected value");
        user.setImage(imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtil.compressImage(file.getBytes()))
                .build()));

        userService.saveUser(user, role.equals("teacher") ? 2 : 3);
        return new ModelAndView("redirect:/admin/users?role=" + role);
    }

    @GetMapping("/users/delete/{id}")
    public ModelAndView deleteUser(@PathVariable int id) {
        ModelAndView mav = getModelAndView(id);
        userService.deleteUser(id);
        return mav;
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView editUser(@PathVariable int id) {
        ModelAndView mav = new ModelAndView("administration/users/editUser");
        mav.addObject("user", userService.getUserById(id));
        return mav;
    }

    @PostMapping("/users/edit/{id}")
    public ModelAndView updateUser(@PathVariable int id, @ModelAttribute("user") User user) {
        ModelAndView mav = getModelAndView(id);
        userService.updateUser(id, user);
        return mav;
    }

    @PostMapping("setAccountStatus/{id}")
    public ModelAndView updateUserStatus(@PathVariable int id) {
        ModelAndView mav = getModelAndView(id);
        if (!userService.getUserById(id).isBlocked()) {
            userService.blockUser(id);
        } else {
            userService.unblockUser(id);
        }
        return mav;
    }

    private ModelAndView getModelAndView(@PathVariable int id) {
        ModelAndView mav = new ModelAndView("administration/adminPage");

        List<Role> roles = userService.getUserById(id).getRoles();
        for (Role userRoles : roles) {
            if (userRoles.getRoleName().equals("TEACHER")) {
                mav = new ModelAndView("redirect:/admin/users?role=teacher");
            }
            if (userRoles.getRoleName().equals("STUDENT")) {
                mav = new ModelAndView("redirect:/admin/users?role=student");
            }
        }
        return mav;
    }

    // Course controller
    @GetMapping("/courses")
    public ModelAndView listCourses(Model model) {

        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("administration/courses/coursesInfo");
    }

    @GetMapping("/courses/addCourse")
    public ModelAndView addCourseForm(Model model) {

        Course course = new Course();
        model.addAttribute("course", course);

        List<UserDTO> teachers = userService.getAllUsersByRole("TEACHER");
        model.addAttribute("teachers", teachers);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("administration/courses/addCourse");
    }

    @PostMapping("/courses/addCourse")
    public ModelAndView saveCourse(@ModelAttribute("course") Course course) {
        courseService.addCourse(course);
        return new ModelAndView("redirect:/admin/courses");
    }

    @GetMapping("/courses/edit/{id}")
    public ModelAndView editCourse(@PathVariable int id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        List<UserDTO> teachers = userService.getAllUsersByRole("TEACHER");
        model.addAttribute("teachers", teachers);

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return new ModelAndView("administration/courses/editCourse");
    }

    @PostMapping("/courses/edit/{id}")
    public ModelAndView updateCourse(@PathVariable int id, @ModelAttribute("course") Course course) {
        courseService.updateCourse(id, course);

        return new ModelAndView("redirect:/admin/courses");
    }

    @GetMapping("/courses/delete/{id}")
    public ModelAndView deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return new ModelAndView("redirect:/admin/courses");
    }

    // topic controller

    @GetMapping("/topics")
    public ModelAndView listTopics(Model model) {
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);

        return new ModelAndView("administration/topics/topicInfo");
    }

    @GetMapping("/topics/add-topic")
    public ModelAndView addTopicForm(Model model) {

        Topic topic = new Topic();
        model.addAttribute("topic", topic);

        return new ModelAndView("administration/topics/addTopic");
    }

    @PostMapping("/topics/add-topic")
    public ModelAndView saveTopic(@RequestParam(value = "image", required = false) MultipartFile file, @ModelAttribute("topic") Topic topic,
                                 Errors validationErrors) throws IOException {

        validationErrors.rejectValue("file", "rejected value");
        topic.setImage(imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtil.compressImage(file.getBytes()))
                .build()));

        topicService.addTopic(topic);
        return new ModelAndView("redirect:/admin/topics");
    }

    @GetMapping("/topics/delete/{id}")
    public ModelAndView deleteTopic(@PathVariable int id) {
        topicService.deleteTopic(id);
        return new ModelAndView("redirect:/admin/topics");
    }

}
