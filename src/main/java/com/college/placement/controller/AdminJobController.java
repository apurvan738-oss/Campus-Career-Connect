package com.college.placement.controller;

import com.college.placement.dto.JobPostingDto;
import com.college.placement.entity.Company;
import com.college.placement.entity.JobApplication;
import com.college.placement.entity.JobPosting;
import com.college.placement.entity.JobStatus;
import com.college.placement.service.CompanyService;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/officer/jobs")
public class AdminJobController {

    private final JobPostingService jobPostingService;
    private final CompanyService companyService;
    private final JobApplicationService applicationService;

    public AdminJobController(JobPostingService jobPostingService,
                              CompanyService companyService,
                              JobApplicationService applicationService) {
        this.jobPostingService = jobPostingService;
        this.companyService = companyService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public String listJobs(Model model) {
        List<JobPosting> jobs = jobPostingService.getAllJobs();
        model.addAttribute("jobs", jobs);
        return "officer/jobs/list";
    }

    @GetMapping("/new")
    public String newJobForm(Model model) {
        if (!model.containsAttribute("jobDto")) {
            model.addAttribute("jobDto", new JobPostingDto());
        }
        model.addAttribute("companies", companyService.getAllActiveCompanies());
        return "officer/jobs/form";
    }

    @PostMapping("/new")
    public String createJob(@Valid @ModelAttribute("jobDto") JobPostingDto dto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.getAllActiveCompanies());
            return "officer/jobs/form";
        }
        jobPostingService.createJob(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Job posting created successfully!");
        return "redirect:/officer/jobs";
    }

    @GetMapping("/{id}/edit")
    public String editJobForm(@PathVariable("id") Long id, Model model) {
        JobPosting job = jobPostingService.getJobById(id);

        JobPostingDto dto = new JobPostingDto();
        dto.setId(job.getId());
        dto.setCompanyId(job.getCompany().getId());
        dto.setTitle(job.getTitle());
        dto.setJobCode(job.getJobCode());
        dto.setDescription(job.getDescription());
        dto.setJobType(job.getJobType());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setLocation(job.getLocation());
        dto.setSalaryPackageLpa(job.getSalaryPackageLpa());
        dto.setMinimumCgpa(job.getMinimumCgpa());
        dto.setEligibleBranches(job.getEligibleBranches());
        dto.setGraduationYear(job.getGraduationYear());
        dto.setVacancies(job.getVacancies());
        dto.setApplicationDeadline(job.getApplicationDeadline());
        dto.setSelectionProcess(job.getSelectionProcess());
        dto.setStatus(job.getStatus());

        model.addAttribute("jobDto", dto);
        model.addAttribute("companies", companyService.getAllActiveCompanies());
        return "officer/jobs/form";
    }

    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("jobDto") JobPostingDto dto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.getAllActiveCompanies());
            return "officer/jobs/form";
        }
        jobPostingService.updateJob(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Job posting updated successfully!");
        return "redirect:/officer/jobs";
    }

    @PostMapping("/{id}/status")
    public String changeJobStatus(@PathVariable("id") Long id,
                                  @RequestParam("status") JobStatus status,
                                  RedirectAttributes redirectAttributes) {
        jobPostingService.toggleJobStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Job status updated to " + status);
        return "redirect:/officer/jobs";
    }

    @PostMapping("/{id}/toggle")
    public String toggleJobActive(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        jobPostingService.toggleActive(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job active status toggled successfully.");
        return "redirect:/officer/jobs";
    }

    @GetMapping("/{id}/applicants")
    public String viewApplicants(@PathVariable("id") Long id, Model model) {
        JobPosting job = jobPostingService.getJobById(id);
        List<JobApplication> applications = applicationService.getApplicationsForJob(id);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "officer/jobs/applicants";
    }
}
