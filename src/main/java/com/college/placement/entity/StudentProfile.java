package com.college.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_profiles", indexes = {
    @Index(name = "idx_student_roll", columnList = "rollNumber"),
    @Index(name = "idx_student_branch", columnList = "branch"),
    @Index(name = "idx_student_grad_year", columnList = "graduationYear"),
    @Index(name = "idx_student_cgpa", columnList = "cgpa")
})
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, unique = true, length = 30)
    private String rollNumber;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String department;

    @Column(nullable = false, length = 50)
    private String branch; // CSE, ECE, IT, ME, EE, CIVIL, etc.

    @Column(nullable = false)
    private Integer graduationYear; // 2024, 2025, 2026

    @Column(nullable = false)
    private Double cgpa; // 0.00 to 10.00

    private Double tenthPercentage;

    private Double twelfthPercentage;

    @Column(columnDefinition = "TEXT")
    private String skills; // e.g., "Java, Spring Boot, MySQL, Git, Docker"

    @Column(length = 255)
    private String resumeFileName;

    @Column(length = 100)
    private String resumeContentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] resumeData;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 20)
    private String gender;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private boolean profileComplete = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        checkCompleteness();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        checkCompleteness();
    }

    public void checkCompleteness() {
        this.profileComplete = (fullName != null && !fullName.isBlank())
                && (rollNumber != null && !rollNumber.isBlank())
                && (branch != null && !branch.isBlank())
                && (graduationYear != null)
                && (cgpa != null)
                && (phone != null && !phone.isBlank())
                && (skills != null && !skills.isBlank());
    }

    public int getProfileCompletenessScore() {
        int score = 0;
        if (fullName != null && !fullName.isBlank()) score += 15;
        if (rollNumber != null && !rollNumber.isBlank()) score += 15;
        if (phone != null && !phone.isBlank()) score += 10;
        if (branch != null && !branch.isBlank()) score += 15;
        if (graduationYear != null) score += 10;
        if (cgpa != null && cgpa > 0) score += 15;
        if (skills != null && !skills.isBlank()) score += 10;
        if (resumeFileName != null && !resumeFileName.isBlank()) score += 10;
        return Math.min(score, 100);
    }

    public StudentProfile() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public Double getTenthPercentage() {
        return tenthPercentage;
    }

    public void setTenthPercentage(Double tenthPercentage) {
        this.tenthPercentage = tenthPercentage;
    }

    public Double getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public void setTwelfthPercentage(Double twelfthPercentage) {
        this.twelfthPercentage = twelfthPercentage;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getResumeContentType() {
        return resumeContentType;
    }

    public void setResumeContentType(String resumeContentType) {
        this.resumeContentType = resumeContentType;
    }

    public byte[] getResumeData() {
        return resumeData;
    }

    public void setResumeData(byte[] resumeData) {
        this.resumeData = resumeData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isProfileComplete() {
        return profileComplete;
    }

    public void setProfileComplete(boolean profileComplete) {
        this.profileComplete = profileComplete;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<JobApplication> applications) {
        this.applications = applications;
    }
}
