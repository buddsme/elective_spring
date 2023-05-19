package com.elective.controller;

import com.elective.entity.dto.UserCourseJournalDTO;
import com.elective.service.CourseService;
import com.elective.service.TopicService;
import com.elective.service.UserCoursesJournalService;
import com.elective.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class JournalController {

    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;
    private final UserCoursesJournalService userCoursesJournalService;

    public JournalController(CourseService courseService, UserService userService, TopicService topicService, UserCoursesJournalService userCoursesJournalService) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.userCoursesJournalService = userCoursesJournalService;
    }

    @GetMapping("/main-page/profile/journal")
    public ModelAndView showJournal(@RequestParam("courseName") String courseName, Model model, Principal principal){
        int userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("userId", userId);

        List<UserCourseJournalDTO> userCourseJournalDTOs = userCoursesJournalService.findAllJournalsByCourseName(courseName);
        model.addAttribute("journals", userCourseJournalDTOs);
        return new ModelAndView("client/journal");
    }

    @PostMapping("/main-page/profile/update-user-grade")
    public ModelAndView updateJournal(@RequestParam("journalId") int journalId, @RequestParam("grade") String newGrade) {
        int grade = Integer.parseInt(newGrade);
        userCoursesJournalService.updateUserGrade(journalId, grade);
        String courseName = courseService.getCourseNameByJournalId(journalId);
        return new ModelAndView("redirect:/main-page/profile/journal?courseName=" + courseName);
    }

}
