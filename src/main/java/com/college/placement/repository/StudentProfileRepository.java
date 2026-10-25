package com.college.placement.repository;

import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUser(User user);

    Optional<StudentProfile> findByUserEmailIgnoreCase(String email);

    Optional<StudentProfile> findByRollNumberIgnoreCase(String rollNumber);

    boolean existsByRollNumberIgnoreCase(String rollNumber);

    @Query("SELECT s FROM StudentProfile s WHERE " +
           "(:keyword IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.skills) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:branch IS NULL OR :branch = '' OR s.branch = :branch) AND " +
           "(:gradYear IS NULL OR s.graduationYear = :gradYear) AND " +
           "(:minCgpa IS NULL OR s.cgpa >= :minCgpa) " +
           "ORDER BY s.fullName ASC")
    List<StudentProfile> filterStudents(
            @Param("keyword") String keyword,
            @Param("branch") String branch,
            @Param("gradYear") Integer gradYear,
            @Param("minCgpa") Double minCgpa
    );

    @Query("SELECT DISTINCT s.branch FROM StudentProfile s WHERE s.branch IS NOT NULL ORDER BY s.branch ASC")
    List<String> findDistinctBranches();

    @Query("SELECT DISTINCT s.graduationYear FROM StudentProfile s WHERE s.graduationYear IS NOT NULL ORDER BY s.graduationYear DESC")
    List<Integer> findDistinctGraduationYears();

    @Query("SELECT COUNT(s) FROM StudentProfile s WHERE s.profileComplete = true")
    long countCompletedProfiles();
}
