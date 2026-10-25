package com.college.placement.repository;

import com.college.placement.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {

    List<InterviewSchedule> findByActiveTrueAndScheduleDateTimeAfterOrderByScheduleDateTimeAsc(LocalDateTime after);

    List<InterviewSchedule> findByJobPostingIdOrderByScheduleDateTimeAsc(Long jobPostingId);
}
