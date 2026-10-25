package com.college.placement.entity;

public enum ApplicationStatus {
    APPLIED("Applied", "bg-primary text-white"),
    SHORTLISTED("Shortlisted", "bg-warning text-dark"),
    SELECTED("Selected", "bg-success text-white"),
    REJECTED("Rejected", "bg-danger text-white"),
    WITHDRAWN("Withdrawn", "bg-secondary text-white");

    private final String displayName;
    private final String badgeClass;

    ApplicationStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public boolean isTerminal() {
        return this == SELECTED || this == REJECTED || this == WITHDRAWN;
    }
}
