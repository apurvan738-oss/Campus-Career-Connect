package com.college.placement.repository;

import com.college.placement.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByActiveTrueOrderByPriorityDescPublishedAtDesc();

    List<Announcement> findTop5ByActiveTrueOrderByPublishedAtDesc();
}
