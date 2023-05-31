package com.elective.controller;

import com.elective.entity.Course;
import com.elective.entity.User;
import com.elective.entity.dto.UserDTO;
import com.elective.entity.dto.UserWithImageDTO;
import com.elective.entity.enums.CourseStatus;
import com.elective.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;
    private final UserCoursesJournalService userCoursesJournalService;

    public UserController(CourseService courseService, UserService userService, TopicService topicService, UserCoursesJournalService userCoursesJournalService) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.userCoursesJournalService = userCoursesJournalService;
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
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/main-page/teachers")
    public ModelAndView showAllTeachers(Model model, Principal principal) {
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        List<UserDTO> teachers = userService.getAllUsersByRole("TEACHER");
        List<UserWithImageDTO> teachersWithImage = userService.setImagesForTeachers(teachers);

        model.addAttribute("teachers", teachersWithImage);
        return new ModelAndView("client/teachers");
    }

    @GetMapping("/main-page/profile")
    public ModelAndView showProfile(@RequestParam(required = false) String filter, @RequestParam("userId") int id, Model model, Principal principal) {
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        UserDTO userDTO = userService.getUserDTOById(id);
        UserWithImageDTO userWithImageDTO = userService.setImagesForUser(userDTO);
        model.addAttribute("user", userWithImageDTO);

        String roleUpperCase = userService.getUserById(id).getRoles().get(0).getRoleName();
        String roleCapitalized = roleUpperCase.charAt(0) + roleUpperCase.substring(1).toLowerCase();
        model.addAttribute("role", roleCapitalized);

        if (roleUpperCase.equals("TEACHER")) {
            List<Course> teacherCourses = courseService.getAllCoursesByTeacherId(id);

            teacherCourses = courseService.checkCoursesStatus(teacherCourses);

            if (!(filter == null) && !filter.equals("ALL")) {
                CourseStatus filterStatus = CourseStatus.valueOf(filter);
                teacherCourses = teacherCourses
                        .stream()
                        .filter(e -> e.getCourseStatus() == filterStatus)
                        .collect(Collectors.toList());
            }

            List<List<Course>> chunkedCourses = getChunkedCourses(teacherCourses);
            if(!chunkedCourses.isEmpty()){
                model.addAttribute("chunkedCourses", chunkedCourses);
            }else{
                model.addAttribute("chunkedCourses", new ArrayList<List<Course>>());
            }

        } else if (roleUpperCase.equals("STUDENT")) {
            List<Course> studentCourses = courseService.getAllCoursesByStudentId(id);

            studentCourses = courseService.checkCoursesStatus(studentCourses);
            if(filter == null){
                filter = "ALL";
            }
            if (!filter.equals("ALL")) {
                CourseStatus filterStatus = CourseStatus.valueOf(filter);
                studentCourses = studentCourses
                        .stream()
                        .filter(e -> e.getCourseStatus() == filterStatus)
                        .collect(Collectors.toList());
            }

            if (filter.equals("FINISHED") || filter.equals("ALL")) {
                for (Course course : studentCourses) {
                    String grade = courseService.getGradeOfCourse(course.getCourseName(), String.valueOf(userId));
                    course.setGrade(grade);

                }
            }

            List<List<Course>> chunkedCourses = getChunkedCourses(studentCourses);

            if(!chunkedCourses.isEmpty()){
                model.addAttribute("chunkedCourses", chunkedCourses);
            }else{
                model.addAttribute("chunkedCourses", new ArrayList<List<Course>>());
            }
        }
        return new ModelAndView("client/profile");
    }

    private List<List<Course>> getChunkedCourses(List<Course> courses) {
        List<List<Course>> chunkedCourses = new ArrayList<>();
        int chunkSize = 5;
        for (int i = 0; i < courses.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, courses.size());
            List<Course> chunk = new ArrayList<>(courses.subList(i, endIndex));
            chunkedCourses.add(chunk);
        }
        return chunkedCourses;
    }

    @GetMapping("/main-page/profile/edit")
    public ModelAndView editProfileView(Model model, Principal principal) {
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        UserDTO userDTO = userService.getUserDTOById(userId);
        UserWithImageDTO userWithImageDTO = userService.setImagesForUser(userDTO);
        model.addAttribute("user", userWithImageDTO);

        String roleUpperCase = userService.getUserById(userId).getRoles().get(0).getRoleName();
        String roleCapitalized = roleUpperCase.charAt(0) + roleUpperCase.substring(1).toLowerCase();
        model.addAttribute("role", roleCapitalized);

        if (roleUpperCase.equals("TEACHER")) {
            List<Course> teacherCourses = courseService.getAllCoursesByTeacherId(userId);
            List<List<Course>> chunkedCourses = getChunkedCourses(teacherCourses);
            model.addAttribute("chunkedCourses", chunkedCourses);
        } else if (roleUpperCase.equals("STUDENT")) {
            List<Course> studentCourses = courseService.getAllCoursesByStudentId(userId);
            List<List<Course>> chunkedCourses = getChunkedCourses(studentCourses);
            model.addAttribute("chunkedCourses", chunkedCourses);
        }
        return new ModelAndView("client/edit-profile");
    }

    @PostMapping("/main-page/profile/edit/{id}")
    public ModelAndView editProfile(@PathVariable("id") int id, @ModelAttribute("user") UserWithImageDTO user) {
        userService.updateUserByUserDTO(user);
        return new ModelAndView("redirect:/main-page/profile?userId=" + id);
    }

    @PostMapping("/profile/cancel-course")
    public ModelAndView deleteUserOnCourse(@RequestParam("courseId") int courseId, Principal principal){
        int userId = userService.getUserIdByEmail(principal.getName());
        userCoursesJournalService.deleteUserOnCourse(courseId, userId);
        return new ModelAndView("redirect:/main-page/profile?userId=" + userId);
    }
}
