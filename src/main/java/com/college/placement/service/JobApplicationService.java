package com.college.placement.service;

import com.college.placement.dto.ApplicationStatusUpdateDto;
import com.college.placement.dto.EligibilityResultDto;
import com.college.placement.entity.*;
import com.college.placement.exception.*;
import com.college.placement.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobPostingService jobPostingService;
    private final StudentProfileService studentProfileService;

    public JobApplicationService(JobApplicationRepository applicationRepository,
                                 JobPostingService jobPostingService,
                                 StudentProfileService studentProfileService) {
        this.applicationRepository = applicationRepository;
        this.jobPostingService = jobPostingService;
        this.studentProfileService = studentProfileService;
    }

    @Transactional
    public JobApplication applyToJob(String studentEmail, Long jobId, String studentNotes) {
        StudentProfile student = studentProfileService.getProfileByEmail(studentEmail);
        JobPosting job = jobPostingService.getJobById(jobId);

        // 1. Prevent duplicate applications
        if (applicationRepository.existsByStudentIdAndJobPostingId(student.getId(), job.getId())) {
            throw new DuplicateApplicationException("You have already submitted an application for " + job.getTitle() + " at " + job.getCompany().getName());
        }

        // 2. Perform server-side eligibility check
        EligibilityResultDto eligibility = jobPostingService.checkEligibility(student, job);
        if (!eligibility.isEligible()) {
            throw new IneligibleApplicationException("Eligibility verification failed: " + eligibility.getFormattedReasons(), eligibility.getReasons());
        }

        // 3. Create application
        JobApplication application = new JobApplication(student, job);
        application.setStudentNotes(studentNotes);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setApplicationDate(LocalDateTime.now());
        application.setLastStatusUpdate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    @Transactional
    public void withdrawApplication(String studentEmail, Long applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        // Insecure direct object reference check
        if (!application.getStudent().getUser().getEmail().equalsIgnoreCase(studentEmail)) {
            throw new UnauthorizedAccessException("You are not authorized to withdraw this application.");
        }

        if (application.getStatus() != ApplicationStatus.APPLIED) {
            throw new InvalidStatusTransitionException("Applications can only be withdrawn while in 'APPLIED' status. Current status is " + application.getStatus());
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setOfficerNotes("Withdrawn by applicant on " + LocalDateTime.now());
        applicationRepository.save(application);
    }

    @Transactional
    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatusUpdateDto dto) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        ApplicationStatus currentStatus = application.getStatus();
        ApplicationStatus newStatus = dto.getStatus();

        // Validate state transitions
        if (currentStatus == ApplicationStatus.WITHDRAWN) {
            throw new InvalidStatusTransitionException("Cannot update status of a withdrawn application.");
        }

        if (currentStatus == ApplicationStatus.SELECTED && newStatus == ApplicationStatus.APPLIED) {
            throw new InvalidStatusTransitionException("Invalid status transition: A selected candidate cannot be reset directly to Applied.");
        }

        application.setStatus(newStatus);
        if (dto.getOfficerNotes() != null && !dto.getOfficerNotes().isBlank()) {
            application.setOfficerNotes(dto.getOfficerNotes().trim());
        }
        if (dto.getInterviewRoundsCleared() != null) {
            application.setInterviewRoundsCleared(dto.getInterviewRoundsCleared());
        }
        application.setLastStatusUpdate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsForStudent(String studentEmail) {
        return applicationRepository.findByStudentUserEmailIgnoreCaseOrderByApplicationDateDesc(studentEmail);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobPostingIdOrderByApplicationDateDesc(jobId);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> filterApplications(Long jobId, ApplicationStatus status, String branch, String keyword) {
        return applicationRepository.filterApplications(jobId, status, branch, keyword);
    }

    @Transactional(readOnly = true)
    public Optional<JobApplication> getExistingApplication(Long studentId, Long jobId) {
        return applicationRepository.findByStudentIdAndJobPostingId(studentId, jobId);
    }

    @Transactional(readOnly = true)
    public JobApplication getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));
    }
}
