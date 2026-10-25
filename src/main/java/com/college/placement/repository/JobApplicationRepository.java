package com.college.placement.repository;

import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStudentIdOrderByApplicationDateDesc(Long studentId);

    List<JobApplication> findByStudentUserEmailIgnoreCaseOrderByApplicationDateDesc(String email);

    List<JobApplication> findByJobPostingIdOrderByApplicationDateDesc(Long jobPostingId);

    Optional<JobApplication> findByStudentIdAndJobPostingId(Long studentId, Long jobPostingId);

    boolean existsByStudentIdAndJobPostingId(Long studentId, Long jobPostingId);

    long countByStatus(ApplicationStatus status);

    long countByStudentIdAndStatus(Long studentId, ApplicationStatus status);

    @Query("SELECT COUNT(DISTINCT a.student.id) FROM JobApplication a WHERE a.status = 'SELECTED'")
    long countDistinctPlacedStudents();

    @Query("SELECT a.student.branch, COUNT(DISTINCT a.student.id) FROM JobApplication a WHERE a.status = 'SELECTED' GROUP BY a.student.branch")
    List<Object[]> countPlacedStudentsByBranch();

    @Query("SELECT a.jobPosting.company.name, COUNT(a.id) FROM JobApplication a WHERE a.status = 'SELECTED' GROUP BY a.jobPosting.company.name")
    List<Object[]> countSelectionsByCompany();

    @Query("SELECT a.status, COUNT(a.id) FROM JobApplication a GROUP BY a.status")
    List<Object[]> countApplicationsByStatusGroup();

    @Query("SELECT a FROM JobApplication a " +
           "JOIN FETCH a.student s " +
           "JOIN FETCH a.jobPosting j " +
           "JOIN FETCH j.company c " +
           "WHERE (:jobId IS NULL OR j.id = :jobId) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:branch IS NULL OR :branch = '' OR s.branch = :branch) " +
           "AND (:keyword IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY a.applicationDate DESC")
    List<JobApplication> filterApplications(
            @Param("jobId") Long jobId,
            @Param("status") ApplicationStatus status,
            @Param("branch") String branch,
            @Param("keyword") String keyword
    );
}
