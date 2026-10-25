package com.college.placement.dto;

import com.college.placement.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class ApplicationStatusUpdateDto {

    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;

    private String officerNotes;

    private Integer interviewRoundsCleared;

    public ApplicationStatusUpdateDto() {}

    public ApplicationStatusUpdateDto(ApplicationStatus status, String officerNotes) {
        this.status = status;
        this.officerNotes = officerNotes;
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

    public Integer getInterviewRoundsCleared() {
        return interviewRoundsCleared;
    }

    public void setInterviewRoundsCleared(Integer interviewRoundsCleared) {
        this.interviewRoundsCleared = interviewRoundsCleared;
    }
}
