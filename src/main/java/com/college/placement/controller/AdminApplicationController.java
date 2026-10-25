package com.college.placement.controller;

import com.college.placement.dto.ApplicationStatusUpdateDto;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.JobApplication;
import com.college.placement.entity.JobPosting;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.JobPostingService;
import com.college.placement.service.ReportService;
import com.college.placement.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@RequestMapping("/officer/applications")
public class AdminApplicationController {

    private final JobApplicationService applicationService;
    private final JobPostingService jobPostingService;
    private final StudentProfileService studentProfileService;
    private final ReportService reportService;

    public AdminApplicationController(JobApplicationService applicationService,
                                    JobPostingService jobPostingService,
                                    StudentProfileService studentProfileService,
                                    ReportService reportService) {
        this.applicationService = applicationService;
        this.jobPostingService = jobPostingService;
        this.studentProfileService = studentProfileService;
        this.reportService = reportService;
    }

    @GetMapping
    public String listApplications(@RequestParam(value = "jobId", required = false) Long jobId,
                                   @RequestParam(value = "status", required = false) ApplicationStatus status,
                                   @RequestParam(value = "branch", required = false) String branch,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   Model model) {
        List<JobApplication> applications = applicationService.filterApplications(jobId, status, branch, keyword);
        List<JobPosting> jobs = jobPostingService.getAllJobs();
        List<String> branches = studentProfileService.getAllBranches();

        model.addAttribute("applications", applications);
        model.addAttribute("jobs", jobs);
        model.addAttribute("branches", branches);
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("selectedJobId", jobId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedBranch", branch);
        model.addAttribute("keyword", keyword);

        return "officer/applications/list";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @Valid @ModelAttribute ApplicationStatusUpdateDto dto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateApplicationStatus(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Application status updated successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/officer/applications";
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportApplications(
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "status", required = false) ApplicationStatus status,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "keyword", required = false) String keyword) {

        List<JobApplication> applications = applicationService.filterApplications(jobId, status, branch, keyword);
        ByteArrayInputStream csvStream = reportService.generateApplicationsCsv(applications);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=placement_applications_" + System.currentTimeMillis() + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csvStream));
    }
}
