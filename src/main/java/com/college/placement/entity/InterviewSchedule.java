package com.college.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_schedules", indexes = {
    @Index(name = "idx_interview_job", columnList = "job_posting_id"),
    @Index(name = "idx_interview_time", columnList = "scheduleDateTime")
})
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(nullable = false, length = 100)
    private String roundName; // Online Coding Test, Technical Interview 1, HR Interview

    @Column(nullable = false)
    private LocalDateTime scheduleDateTime;

    @Column(length = 255)
    private String venueOrLink; // Lab 3, Campus Audi, or Google Meet / Teams link

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public InterviewSchedule() {}

    public InterviewSchedule(JobPosting jobPosting, String roundName, LocalDateTime scheduleDateTime, String venueOrLink) {
        this.jobPosting = jobPosting;
        this.roundName = roundName;
        this.scheduleDateTime = scheduleDateTime;
        this.venueOrLink = venueOrLink;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public LocalDateTime getScheduleDateTime() {
        return scheduleDateTime;
    }

    public void setScheduleDateTime(LocalDateTime scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public String getVenueOrLink() {
        return venueOrLink;
    }

    public void setVenueOrLink(String venueOrLink) {
        this.venueOrLink = venueOrLink;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
