package com.college.placement.controller;

import com.college.placement.dto.PlacementStatsDto;
import com.college.placement.entity.Announcement;
import com.college.placement.entity.JobPosting;
import com.college.placement.service.AnnouncementService;
import com.college.placement.service.CompanyService;
import com.college.placement.service.JobPostingService;
import com.college.placement.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ReportService reportService;
    private final JobPostingService jobPostingService;
    private final AnnouncementService announcementService;
    private final CompanyService companyService;

    public HomeController(ReportService reportService,
                          JobPostingService jobPostingService,
                          AnnouncementService announcementService,
                          CompanyService companyService) {
        this.reportService = reportService;
        this.jobPostingService = jobPostingService;
        this.announcementService = announcementService;
        this.companyService = companyService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        PlacementStatsDto stats = reportService.getPlacementStats();
        List<JobPosting> openJobs = jobPostingService.getRecentOpenJobs();
        List<Announcement> announcements = announcementService.getRecentAnnouncements();

        model.addAttribute("stats", stats);
        model.addAttribute("openJobs", openJobs);
        model.addAttribute("announcements", announcements);
        model.addAttribute("companies", companyService.getAllActiveCompanies());

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        PlacementStatsDto stats = reportService.getPlacementStats();
        model.addAttribute("stats", stats);
        return "about";
    }

    @GetMapping("/guidelines")
    public String guidelines() {
        return "guidelines";
    }

    @GetMapping("/announcements")
    public String publicAnnouncements(Model model) {
        model.addAttribute("announcements", announcementService.getActiveAnnouncements());
        return "announcements";
    }
}
