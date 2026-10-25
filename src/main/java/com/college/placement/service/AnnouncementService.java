package com.college.placement.service;

import com.college.placement.entity.Announcement;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Transactional(readOnly = true)
    public List<Announcement> getActiveAnnouncements() {
        return announcementRepository.findByActiveTrueOrderByPriorityDescPublishedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Announcement> getRecentAnnouncements() {
        return announcementRepository.findTop5ByActiveTrueOrderByPublishedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @Transactional
    public Announcement saveAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Transactional
    public void toggleActive(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with ID: " + id));
        announcement.setActive(!announcement.isActive());
        announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}
