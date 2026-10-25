package com.college.placement.controller;

import com.college.placement.dto.PlacementStatsDto;
import com.college.placement.entity.JobApplication;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/officer")
public class AdminController {

    private final ReportService reportService;
    private final JobApplicationService applicationService;

    public AdminController(ReportService reportService, JobApplicationService applicationService) {
        this.reportService = reportService;
        this.applicationService = applicationService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        PlacementStatsDto stats = reportService.getPlacementStats();
        List<JobApplication> recentApplications = applicationService.filterApplications(null, null, null, null);
        if (recentApplications.size() > 8) {
            recentApplications = recentApplications.subList(0, 8);
        }

        model.addAttribute("stats", stats);
        model.addAttribute("recentApplications", recentApplications);

        return "officer/dashboard";
    }
}
