package com.college.placement.controller;

import com.college.placement.dto.EligibilityResultDto;
import com.college.placement.dto.JobFilterDto;
import com.college.placement.entity.Company;
import com.college.placement.entity.JobApplication;
import com.college.placement.entity.JobPosting;
import com.college.placement.entity.StudentProfile;
import com.college.placement.exception.DuplicateApplicationException;
import com.college.placement.exception.IneligibleApplicationException;
import com.college.placement.service.CompanyService;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.JobPostingService;
import com.college.placement.service.StudentProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class JobController {

    private final JobPostingService jobPostingService;
    private final CompanyService companyService;
    private final StudentProfileService studentProfileService;
    private final JobApplicationService applicationService;

    public JobController(JobPostingService jobPostingService,
                         CompanyService companyService,
                         StudentProfileService studentProfileService,
                         JobApplicationService applicationService) {
        this.jobPostingService = jobPostingService;
        this.companyService = companyService;
        this.studentProfileService = studentProfileService;
        this.applicationService = applicationService;
    }

    @GetMapping("/jobs")
    public String browseJobs(@ModelAttribute("filter") JobFilterDto filter,
                             Model model,
                             Authentication authentication) {
        List<JobPosting> jobs = jobPostingService.filterJobs(filter);
        List<Company> companies = companyService.getAllActiveCompanies();

        // If authenticated student, pre-compute eligibility for each job to display badge
        Map<Long, EligibilityResultDto> eligibilityMap = new HashMap<>();
        Map<Long, Boolean> appliedMap = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            boolean isStudent = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
            if (isStudent) {
                try {
                    StudentProfile student = studentProfileService.getProfileByEmail(authentication.getName());
                    for (JobPosting job : jobs) {
                        EligibilityResultDto res = jobPostingService.checkEligibility(student, job);
                        eligibilityMap.put(job.getId(), res);

                        boolean applied = applicationService.getExistingApplication(student.getId(), job.getId()).isPresent();
                        appliedMap.put(job.getId(), applied);
                    }
                } catch (Exception ignored) {}
            }
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("companies", companies);
        model.addAttribute("filter", filter);
        model.addAttribute("eligibilityMap", eligibilityMap);
        model.addAttribute("appliedMap", appliedMap);

        return "jobs/list";
    }

    @GetMapping("/jobs/{id}")
    public String viewJobDetails(@PathVariable("id") Long id,
                                Model model,
                                Authentication authentication) {
        JobPosting job = jobPostingService.getJobById(id);
        model.addAttribute("job", job);

        boolean isStudent = false;
        boolean hasApplied = false;
        JobApplication existingApp = null;
        EligibilityResultDto eligibility = null;

        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            isStudent = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
            if (isStudent) {
                try {
                    StudentProfile student = studentProfileService.getProfileByEmail(authentication.getName());
                    eligibility = jobPostingService.checkEligibility(student, job);
                    Optional<JobApplication> appOpt = applicationService.getExistingApplication(student.getId(), job.getId());
                    if (appOpt.isPresent()) {
                        hasApplied = true;
                        existingApp = appOpt.get();
                    }
                } catch (Exception ignored) {}
            }
        }

        model.addAttribute("isStudent", isStudent);
        model.addAttribute("hasApplied", hasApplied);
        model.addAttribute("existingApplication", existingApp);
        model.addAttribute("eligibility", eligibility);

        return "jobs/details";
    }

    @PostMapping("/student/jobs/{id}/apply")
    public String applyToJob(@PathVariable("id") Long id,
                             @RequestParam(value = "studentNotes", required = false) String studentNotes,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            applicationService.applyToJob(authentication.getName(), id, studentNotes);
            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully! Track your status on your dashboard.");
            return "redirect:/student/applications";
        } catch (DuplicateApplicationException | IneligibleApplicationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/jobs/" + id;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting application: " + ex.getMessage());
            return "redirect:/jobs/" + id;
        }
    }
}
