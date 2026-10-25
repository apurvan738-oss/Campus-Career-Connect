package com.college.placement.service;

import com.college.placement.entity.InterviewSchedule;
import com.college.placement.entity.JobPosting;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.InterviewScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewScheduleService {

    private final InterviewScheduleRepository scheduleRepository;
    private final JobPostingService jobPostingService;

    public InterviewScheduleService(InterviewScheduleRepository scheduleRepository,
                                    JobPostingService jobPostingService) {
        this.scheduleRepository = scheduleRepository;
        this.jobPostingService = jobPostingService;
    }

    @Transactional(readOnly = true)
    public List<InterviewSchedule> getUpcomingSchedules() {
        return scheduleRepository.findByActiveTrueAndScheduleDateTimeAfterOrderByScheduleDateTimeAsc(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<InterviewSchedule> getSchedulesForJob(Long jobId) {
        return scheduleRepository.findByJobPostingIdOrderByScheduleDateTimeAsc(jobId);
    }

    @Transactional
    public InterviewSchedule createSchedule(Long jobId, String roundName, LocalDateTime scheduleTime, String venue, String instructions) {
        JobPosting job = jobPostingService.getJobById(jobId);
        InterviewSchedule schedule = new InterviewSchedule(job, roundName, scheduleTime, venue);
        schedule.setInstructions(instructions);
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
