package com.college.placement.service;

import com.college.placement.dto.PlacementStatsDto;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.JobApplication;
import com.college.placement.entity.JobStatus;
import com.college.placement.entity.Role;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.JobApplicationRepository;
import com.college.placement.repository.JobPostingRepository;
import com.college.placement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository applicationRepository;

    public ReportService(UserRepository userRepository,
                         CompanyRepository companyRepository,
                         JobPostingRepository jobPostingRepository,
                         JobApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional(readOnly = true)
    public PlacementStatsDto getPlacementStats() {
        PlacementStatsDto stats = new PlacementStatsDto();

        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long placedStudents = applicationRepository.countDistinctPlacedStudents();
        long activeCompanies = companyRepository.countByActiveTrue();
        long openJobs = jobPostingRepository.countByActiveTrueAndStatus(JobStatus.OPEN);
        long totalApplications = applicationRepository.count();
        long shortlistedCount = applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED);
        long selectedCount = applicationRepository.countByStatus(ApplicationStatus.SELECTED);

        stats.setTotalStudents(totalStudents);
        stats.setPlacedStudents(placedStudents);
        stats.setActiveCompanies(activeCompanies);
        stats.setOpenJobs(openJobs);
        stats.setTotalApplications(totalApplications);
        stats.setShortlistedCount(shortlistedCount);
        stats.setSelectedCount(selectedCount);

        double rate = totalStudents > 0 ? ((double) placedStudents / totalStudents) * 100.0 : 0.0;
        stats.setPlacementRate(Math.round(rate * 100.0) / 100.0);

        // Branch-wise placements
        List<Object[]> branchResults = applicationRepository.countPlacedStudentsByBranch();
        Map<String, Long> branchMap = new LinkedHashMap<>();
        for (Object[] row : branchResults) {
            String branch = (String) row[0];
            Long count = (Long) row[1];
            branchMap.put(branch != null ? branch : "Other", count);
        }
        stats.setBranchWisePlacements(branchMap);

        // Company selections
        List<Object[]> companyResults = applicationRepository.countSelectionsByCompany();
        Map<String, Long> companyMap = new LinkedHashMap<>();
        for (Object[] row : companyResults) {
            String company = (String) row[0];
            Long count = (Long) row[1];
            companyMap.put(company, count);
        }
        stats.setCompanyWiseSelections(companyMap);

        // Status distribution
        List<Object[]> statusResults = applicationRepository.countApplicationsByStatusGroup();
        Map<String, Long> statusMap = new LinkedHashMap<>();
        for (Object[] row : statusResults) {
            ApplicationStatus st = (ApplicationStatus) row[0];
            Long count = (Long) row[1];
            statusMap.put(st.getDisplayName(), count);
        }
        stats.setStatusDistribution(statusMap);

        // Salary metrics from placed applications
        List<JobApplication> allSelected = applicationRepository.findAll().stream()
                .filter(a -> a.getStatus() == ApplicationStatus.SELECTED)
                .toList();

        if (!allSelected.isEmpty()) {
            double maxPkg = allSelected.stream()
                    .mapToDouble(a -> a.getJobPosting().getSalaryPackageLpa())
                    .max().orElse(0.0);
            double avgPkg = allSelected.stream()
                    .mapToDouble(a -> a.getJobPosting().getSalaryPackageLpa())
                    .average().orElse(0.0);

            stats.setHighestPackage(Math.round(maxPkg * 100.0) / 100.0);
            stats.setAveragePackage(Math.round(avgPkg * 100.0) / 100.0);
        } else {
            stats.setHighestPackage(0.0);
            stats.setAveragePackage(0.0);
        }

        return stats;
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream generateApplicationsCsv(List<JobApplication> applications) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {
            // Write CSV Header
            writer.println("Application ID,Student Name,Roll Number,Branch,CGPA,Graduation Year,Company,Job Title,Package (LPA),Application Date,Status,Officer Notes");

            for (JobApplication app : applications) {
                writer.printf("\"%d\",\"%s\",\"%s\",\"%s\",\"%.2f\",\"%d\",\"%s\",\"%s\",\"%.2f\",\"%s\",\"%s\",\"%s\"%n",
                        app.getId(),
                        escapeCsv(app.getStudent().getFullName()),
                        escapeCsv(app.getStudent().getRollNumber()),
                        escapeCsv(app.getStudent().getBranch()),
                        app.getStudent().getCgpa() != null ? app.getStudent().getCgpa() : 0.0,
                        app.getStudent().getGraduationYear() != null ? app.getStudent().getGraduationYear() : 0,
                        escapeCsv(app.getJobPosting().getCompany().getName()),
                        escapeCsv(app.getJobPosting().getTitle()),
                        app.getJobPosting().getSalaryPackageLpa(),
                        app.getApplicationDate().toString(),
                        app.getStatus().name(),
                        escapeCsv(app.getOfficerNotes() != null ? app.getOfficerNotes() : "")
                );
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private String escapeCsv(String input) {
        if (input == null) return "";
        return input.replace("\"", "\"\"");
    }
}
