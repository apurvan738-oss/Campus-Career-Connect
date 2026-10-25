package com.college.placement.controller;

import com.college.placement.entity.JobApplication;
import com.college.placement.entity.StudentProfile;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.StudentProfileService;
import com.college.placement.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/officer/students")
public class AdminStudentController {

    private final StudentProfileService profileService;
    private final UserService userService;
    private final JobApplicationService applicationService;

    public AdminStudentController(StudentProfileService profileService,
                                  UserService userService,
                                  JobApplicationService applicationService) {
        this.profileService = profileService;
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public String listStudents(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "branch", required = false) String branch,
                               @RequestParam(value = "gradYear", required = false) Integer gradYear,
                               @RequestParam(value = "minCgpa", required = false) Double minCgpa,
                               Model model) {
        List<StudentProfile> students = profileService.filterStudents(keyword, branch, gradYear, minCgpa);
        List<String> branches = profileService.getAllBranches();
        List<Integer> years = profileService.getAllGraduationYears();

        model.addAttribute("students", students);
        model.addAttribute("branches", branches);
        model.addAttribute("years", years);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedBranch", branch);
        model.addAttribute("selectedYear", gradYear);
        model.addAttribute("minCgpa", minCgpa);

        return "officer/students/list";
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable("id") Long id, Model model) {
        StudentProfile student = profileService.getProfileById(id);
        List<JobApplication> applications = applicationService.getApplicationsForStudent(student.getUser().getEmail());

        model.addAttribute("student", student);
        model.addAttribute("applications", applications);
        return "officer/students/detail";
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<byte[]> downloadStudentResume(@PathVariable("id") Long id) {
        StudentProfile student = profileService.getProfileById(id);
        if (student.getResumeData() == null || student.getResumeFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = student.getResumeContentType() != null ? student.getResumeContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + student.getResumeFileName() + "\"")
                .body(student.getResumeData());
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStudentStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        StudentProfile student = profileService.getProfileById(id);
        userService.toggleUserActiveStatus(student.getUser().getId());
        redirectAttributes.addFlashAttribute("successMessage", "Student account status updated.");
        return "redirect:/officer/students/" + id;
    }
}
