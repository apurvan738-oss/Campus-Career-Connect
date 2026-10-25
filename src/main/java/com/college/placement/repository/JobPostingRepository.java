package com.college.placement.repository;

import com.college.placement.entity.JobPosting;
import com.college.placement.entity.JobStatus;
import com.college.placement.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Optional<JobPosting> findByJobCode(String jobCode);

    List<JobPosting> findByActiveTrueAndStatusOrderByApplicationDeadlineAsc(JobStatus status);

    List<JobPosting> findByCompanyIdOrderByCreatedAtDesc(Long companyId);

    long countByActiveTrueAndStatus(JobStatus status);

    @Query("SELECT j FROM JobPosting j " +
           "JOIN FETCH j.company c " +
           "WHERE j.active = true " +
           "AND (:status IS NULL OR j.status = :status) " +
           "AND (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:location IS NULL OR :location = '' OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:jobType IS NULL OR j.jobType = :jobType) " +
           "AND (:companyId IS NULL OR c.id = :companyId) " +
           "AND (:maxMinCgpa IS NULL OR j.minimumCgpa <= :maxMinCgpa) " +
           "AND (:minPackage IS NULL OR j.salaryPackageLpa >= :minPackage) " +
           "ORDER BY j.applicationDeadline ASC")
    List<JobPosting> searchJobs(
            @Param("keyword") String keyword,
            @Param("status") JobStatus status,
            @Param("location") String location,
            @Param("jobType") JobType jobType,
            @Param("companyId") Long companyId,
            @Param("maxMinCgpa") Double maxMinCgpa,
            @Param("minPackage") Double minPackage
    );

    @Query("SELECT j FROM JobPosting j WHERE j.active = true AND j.status = 'OPEN' AND j.applicationDeadline >= :now ORDER BY j.createdAt DESC")
    List<JobPosting> findRecentOpenJobs(@Param("now") LocalDateTime now);
}
