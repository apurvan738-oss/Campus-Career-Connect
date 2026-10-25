package com.college.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
    @UniqueConstraint(name = "uk_student_job_application", columnNames = {"student_id", "job_posting_id"})
}, indexes = {
    @Index(name = "idx_application_student", columnList = "student_id"),
    @Index(name = "idx_application_job", columnList = "job_posting_id"),
    @Index(name = "idx_application_status", columnList = "status")
})
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(columnDefinition = "TEXT")
    private String officerNotes;

    @Column(columnDefinition = "TEXT")
    private String studentNotes;

    private Integer interviewRoundsCleared = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime applicationDate;

    private LocalDateTime lastStatusUpdate;

    @PrePersist
    protected void onCreate() {
        this.applicationDate = LocalDateTime.now();
        this.lastStatusUpdate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastStatusUpdate = LocalDateTime.now();
    }

    public JobApplication() {}

    public JobApplication(StudentProfile student, JobPosting jobPosting) {
        this.student = student;
        this.jobPosting = jobPosting;
        this.status = ApplicationStatus.APPLIED;
        this.interviewRoundsCleared = 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentProfile getStudent() {
        return student;
    }

    public void setStudent(StudentProfile student) {
        this.student = student;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getOfficerNotes() {
        return officerNotes;
    }

    public void setOfficerNotes(String officerNotes) {
        this.officerNotes = officerNotes;
    }

    public String getStudentNotes() {
        return studentNotes;
    }

    public void setStudentNotes(String studentNotes) {
        this.studentNotes = studentNotes;
    }

    public Integer getInterviewRoundsCleared() {
        return interviewRoundsCleared;
    }

    public void setInterviewRoundsCleared(Integer interviewRoundsCleared) {
        this.interviewRoundsCleared = interviewRoundsCleared;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDateTime getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    public void setLastStatusUpdate(LocalDateTime lastStatusUpdate) {
        this.lastStatusUpdate = lastStatusUpdate;
    }
}
