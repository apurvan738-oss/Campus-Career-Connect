package com.college.placement.entity;

public enum JobStatus {
    OPEN("Open"),
    CLOSED("Closed"),
    ARCHIVED("Archived");

    private final String displayName;

    JobStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
