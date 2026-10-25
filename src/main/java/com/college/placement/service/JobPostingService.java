package com.college.placement.service;

import com.college.placement.dto.EligibilityResultDto;
import com.college.placement.dto.JobFilterDto;
import com.college.placement.dto.JobPostingDto;
import com.college.placement.entity.Company;
import com.college.placement.entity.JobPosting;
import com.college.placement.entity.JobStatus;
import com.college.placement.entity.StudentProfile;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.JobPostingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyService companyService;

    public JobPostingService(JobPostingRepository jobPostingRepository, CompanyService companyService) {
        this.jobPostingRepository = jobPostingRepository;
        this.companyService = companyService;
    }

    @Transactional(readOnly = true)
    public List<JobPosting> getAllOpenJobs() {
        return jobPostingRepository.findByActiveTrueAndStatusOrderByApplicationDeadlineAsc(JobStatus.OPEN);
    }

    @Transactional(readOnly = true)
    public List<JobPosting> getRecentOpenJobs() {
        return jobPostingRepository.findRecentOpenJobs(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<JobPosting> getAllJobs() {
        return jobPostingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public JobPosting getJobById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job opportunity not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<JobPosting> filterJobs(JobFilterDto filter) {
        return jobPostingRepository.searchJobs(
                (filter.getKeyword() != null && !filter.getKeyword().isBlank()) ? filter.getKeyword().trim() : null,
                JobStatus.OPEN,
                filter.getLocation(),
                filter.getJobType(),
                filter.getCompanyId(),
                filter.getMaxMinCgpa(),
                filter.getMinPackage()
        );
    }

    @Transactional
    public JobPosting createJob(JobPostingDto dto) {
        Company company = companyService.getCompanyById(dto.getCompanyId());

        JobPosting job = new JobPosting();
        job.setCompany(company);
        job.setTitle(dto.getTitle().trim());
        job.setDescription(dto.getDescription());
        job.setJobType(dto.getJobType());
        job.setRequiredSkills(dto.getRequiredSkills().trim());
        job.setLocation(dto.getLocation().trim());
        job.setSalaryPackageLpa(dto.getSalaryPackageLpa());
        job.setMinimumCgpa(dto.getMinimumCgpa());
        job.setEligibleBranches(dto.getEligibleBranches().trim().toUpperCase());
        job.setGraduationYear(dto.getGraduationYear());
        job.setVacancies(dto.getVacancies() != null ? dto.getVacancies() : 1);
        job.setApplicationDeadline(dto.getApplicationDeadline());
        job.setSelectionProcess(dto.getSelectionProcess());
        job.setStatus(dto.getStatus() != null ? dto.getStatus() : JobStatus.OPEN);
        job.setActive(true);

        return jobPostingRepository.save(job);
    }

    @Transactional
    public JobPosting updateJob(Long id, JobPostingDto dto) {
        JobPosting job = getJobById(id);
        Company company = companyService.getCompanyById(dto.getCompanyId());

        job.setCompany(company);
        job.setTitle(dto.getTitle().trim());
        job.setDescription(dto.getDescription());
        job.setJobType(dto.getJobType());
        job.setRequiredSkills(dto.getRequiredSkills().trim());
        job.setLocation(dto.getLocation().trim());
        job.setSalaryPackageLpa(dto.getSalaryPackageLpa());
        job.setMinimumCgpa(dto.getMinimumCgpa());
        job.setEligibleBranches(dto.getEligibleBranches().trim().toUpperCase());
        job.setGraduationYear(dto.getGraduationYear());
        job.setVacancies(dto.getVacancies());
        job.setApplicationDeadline(dto.getApplicationDeadline());
        job.setSelectionProcess(dto.getSelectionProcess());
        if (dto.getStatus() != null) {
            job.setStatus(dto.getStatus());
        }

        return jobPostingRepository.save(job);
    }

    @Transactional
    public void toggleJobStatus(Long id, JobStatus newStatus) {
        JobPosting job = getJobById(id);
        job.setStatus(newStatus);
        jobPostingRepository.save(job);
    }

    @Transactional
    public void toggleActive(Long id) {
        JobPosting job = getJobById(id);
        job.setActive(!job.isActive());
        jobPostingRepository.save(job);
    }

    /**
     * Core eligibility evaluation method.
     * Evaluates multiple academic, branch, profile, and timeline conditions.
     */
    public EligibilityResultDto checkEligibility(StudentProfile student, JobPosting job) {
        List<String> reasons = new ArrayList<>();

        if (student == null) {
            reasons.add("Student profile does not exist. Please complete your registration.");
            return EligibilityResultDto.ineligible(reasons);
        }

        // 1. Job active and open / limit over
        if (!job.isActive() || job.getStatus() != JobStatus.OPEN) {
            reasons.add("Application limit / window is over: This drive is closed (Status: " + job.getStatus().getDisplayName() + ").");
        }

        // 2. Application deadline
        if (job.isExpired()) {
            reasons.add("Application deadline is over: The registration window expired on " + job.getApplicationDeadline() + ".");
        }

        // 3. Minimum CGPA
        if (student.getCgpa() == null || student.getCgpa() < job.getMinimumCgpa()) {
            reasons.add(String.format("Your CGPA (%.2f) does not meet the minimum requirement of %.2f.",
                    student.getCgpa() != null ? student.getCgpa() : 0.0,
                    job.getMinimumCgpa()));
        }

        // 4. Branch eligibility
        List<String> eligibleBranches = job.getEligibleBranchList();
        if (!eligibleBranches.contains("ALL")) {
            String studentBranch = student.getBranch() != null ? student.getBranch().trim().toUpperCase() : "";
            if (!eligibleBranches.contains(studentBranch)) {
                reasons.add("Your branch (" + studentBranch + ") is not eligible. Eligible branches: " + job.getEligibleBranches());
            }
        }

        // 5. Graduation Year
        if (job.getGraduationYear() != null && !job.getGraduationYear().equals(student.getGraduationYear())) {
            reasons.add("Targeted graduation year is " + job.getGraduationYear() + ", but your registered batch is " + student.getGraduationYear() + ".");
        }

        // 6. Resume uploaded
        if (student.getResumeFileName() == null || student.getResumeFileName().isBlank()) {
            reasons.add("You must upload a verified resume to your profile before applying.");
        }

        if (reasons.isEmpty()) {
            return EligibilityResultDto.eligible();
        } else {
            return EligibilityResultDto.ineligible(reasons);
        }
    }
}
