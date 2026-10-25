package com.college.placement.controller;

import com.college.placement.entity.Announcement;
import com.college.placement.entity.AnnouncementPriority;
import com.college.placement.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/officer/announcements")
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;

    public AdminAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public String listAnnouncements(Model model) {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        if (!model.containsAttribute("announcement")) {
            model.addAttribute("announcement", new Announcement());
        }
        model.addAttribute("announcements", announcements);
        model.addAttribute("priorities", AnnouncementPriority.values());
        return "officer/announcements/list";
    }

    @PostMapping("/new")
    public String createAnnouncement(@Valid @ModelAttribute("announcement") Announcement announcement,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "officer/announcements/list";
        }
        announcementService.saveAnnouncement(announcement);
        redirectAttributes.addFlashAttribute("successMessage", "Announcement published successfully!");
        return "redirect:/officer/announcements";
    }

    @PostMapping("/{id}/toggle")
    public String toggleAnnouncement(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        announcementService.toggleActive(id);
        redirectAttributes.addFlashAttribute("successMessage", "Announcement visibility toggled.");
        return "redirect:/officer/announcements";
    }

    @PostMapping("/{id}/delete")
    public String deleteAnnouncement(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        announcementService.deleteAnnouncement(id);
        redirectAttributes.addFlashAttribute("successMessage", "Announcement removed successfully.");
        return "redirect:/officer/announcements";
    }
}
