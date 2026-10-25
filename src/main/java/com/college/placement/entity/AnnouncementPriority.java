package com.college.placement.entity;

public enum AnnouncementPriority {
    NORMAL("Normal", "badge bg-info text-dark"),
    HIGH("High", "badge bg-warning text-dark"),
    URGENT("Urgent", "badge bg-danger text-white");

    private final String displayName;
    private final String badgeClass;

    AnnouncementPriority(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
