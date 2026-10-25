package com.college.placement.controller;

import com.college.placement.dto.StudentProfileDto;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.JobApplication;
import com.college.placement.entity.JobPosting;
import com.college.placement.entity.StudentProfile;
import com.college.placement.exception.UnauthorizedAccessException;
import com.college.placement.service.AnnouncementService;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.JobPostingService;
import com.college.placement.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentProfileService profileService;
    private final JobApplicationService applicationService;
    private final JobPostingService jobPostingService;
    private final AnnouncementService announcementService;

    public StudentController(StudentProfileService profileService,
                             JobApplicationService applicationService,
                             JobPostingService jobPostingService,
                             AnnouncementService announcementService) {
        this.profileService = profileService;
        this.applicationService = applicationService;
        this.jobPostingService = jobPostingService;
        this.announcementService = announcementService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        StudentProfile profile = profileService.getProfileByEmail(email);
        List<JobApplication> applications = applicationService.getApplicationsForStudent(email);

        long appliedCount = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.APPLIED).count();
        long shortlistedCount = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED).count();
        long selectedCount = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.SELECTED).count();

        List<JobPosting> openJobs = jobPostingService.getAllOpenJobs();

        model.addAttribute("profile", profile);
        model.addAttribute("completeness", profile.getProfileCompletenessScore());
        model.addAttribute("applications", applications);
        model.addAttribute("appliedCount", appliedCount);
        model.addAttribute("shortlistedCount", shortlistedCount);
        model.addAttribute("selectedCount", selectedCount);
        model.addAttribute("openJobsCount", openJobs.size());
        model.addAttribute("announcements", announcementService.getRecentAnnouncements());

        return "student/dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {
        StudentProfile profile = profileService.getProfileByEmail(authentication.getName());

        if (!model.containsAttribute("profileDto")) {
            StudentProfileDto dto = new StudentProfileDto();
            dto.setFullName(profile.getFullName());
            dto.setRollNumber(profile.getRollNumber());
            dto.setPhone(profile.getPhone());
            dto.setDepartment(profile.getDepartment());
            dto.setBranch(profile.getBranch());
            dto.setGraduationYear(profile.getGraduationYear());
            dto.setCgpa(profile.getCgpa());
            dto.setTenthPercentage(profile.getTenthPercentage());
            dto.setTwelfthPercentage(profile.getTwelfthPercentage());
            dto.setSkills(profile.getSkills());
            dto.setAddress(profile.getAddress());
            dto.setGender(profile.getGender());
            dto.setDateOfBirth(profile.getDateOfBirth());
            model.addAttribute("profileDto", dto);
        }

        model.addAttribute("profile", profile);
        model.addAttribute("completeness", profile.getProfileCompletenessScore());
        model.addAttribute("branches", List.of("CSE", "ECE", "IT", "ME", "EE", "CIVIL", "CHEM"));

        return "student/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileDto") StudentProfileDto dto,
                                BindingResult bindingResult,
                                Model model,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            StudentProfile profile = profileService.getProfileByEmail(authentication.getName());
            model.addAttribute("profile", profile);
            model.addAttribute("completeness", profile.getProfileCompletenessScore());
            model.addAttribute("branches", List.of("CSE", "ECE", "IT", "ME", "EE", "CIVIL", "CHEM"));
            return "student/profile";
        }

        try {
            profileService.updateProfile(authentication.getName(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/student/profile";
    }

    @PostMapping("/resume/upload")
    public String uploadResume(@RequestParam("resumeFile") MultipartFile file,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            profileService.uploadResume(authentication.getName(), file);
            redirectAttributes.addFlashAttribute("successMessage", "Resume uploaded and verified successfully!");
        } catch (IllegalArgumentException | IOException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/student/profile";
    }

    @GetMapping("/resume/download")
    public ResponseEntity<byte[]> downloadResume(Authentication authentication) {
        StudentProfile profile = profileService.getProfileByEmail(authentication.getName());
        if (profile.getResumeData() == null || profile.getResumeFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = profile.getResumeContentType() != null ? profile.getResumeContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profile.getResumeFileName() + "\"")
                .body(profile.getResumeData());
    }

    @GetMapping("/applications")
    public String myApplications(Model model, Authentication authentication) {
        List<JobApplication> applications = applicationService.getApplicationsForStudent(authentication.getName());
        model.addAttribute("applications", applications);
        return "student/applications";
    }

    @PostMapping("/applications/{id}/withdraw")
    public String withdrawApplication(@PathVariable("id") Long id,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        try {
            applicationService.withdrawApplication(authentication.getName(), id);
            redirectAttributes.addFlashAttribute("successMessage", "Application withdrawn successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/student/applications";
    }
}
