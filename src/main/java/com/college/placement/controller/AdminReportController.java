package com.college.placement.controller;

import com.college.placement.dto.PlacementStatsDto;
import com.college.placement.entity.JobApplication;
import com.college.placement.service.JobApplicationService;
import com.college.placement.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@RequestMapping("/officer/reports")
public class AdminReportController {

    private final ReportService reportService;
    private final JobApplicationService applicationService;

    public AdminReportController(ReportService reportService, JobApplicationService applicationService) {
        this.reportService = reportService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public String viewReports(Model model) {
        PlacementStatsDto stats = reportService.getPlacementStats();
        List<JobApplication> allApplications = applicationService.filterApplications(null, null, null, null);

        model.addAttribute("stats", stats);
        model.addAttribute("totalApps", allApplications.size());

        return "officer/reports/index";
    }

    @GetMapping("/export/summary")
    public ResponseEntity<InputStreamResource> exportAllApplicationsSummary() {
        List<JobApplication> all = applicationService.filterApplications(null, null, null, null);
        ByteArrayInputStream csv = reportService.generateApplicationsCsv(all);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=placement_master_report_" + System.currentTimeMillis() + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csv));
    }
}
