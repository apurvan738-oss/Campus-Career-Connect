package com.college.placement.dto;

import java.util.HashMap;
import java.util.Map;

public class PlacementStatsDto {

    private long totalStudents;
    private long placedStudents;
    private long activeCompanies;
    private long openJobs;
    private long totalApplications;
    private long shortlistedCount;
    private long selectedCount;
    private double placementRate;
    private double averagePackage;
    private double highestPackage;

    private Map<String, Long> branchWisePlacements = new HashMap<>();
    private Map<String, Long> companyWiseSelections = new HashMap<>();
    private Map<String, Long> statusDistribution = new HashMap<>();

    public PlacementStatsDto() {}

    // Getters and Setters
    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getPlacedStudents() {
        return placedStudents;
    }

    public void setPlacedStudents(long placedStudents) {
        this.placedStudents = placedStudents;
    }

    public long getActiveCompanies() {
        return activeCompanies;
    }

    public void setActiveCompanies(long activeCompanies) {
        this.activeCompanies = activeCompanies;
    }

    public long getOpenJobs() {
        return openJobs;
    }

    public void setOpenJobs(long openJobs) {
        this.openJobs = openJobs;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getShortlistedCount() {
        return shortlistedCount;
    }

    public void setShortlistedCount(long shortlistedCount) {
        this.shortlistedCount = shortlistedCount;
    }

    public long getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(long selectedCount) {
        this.selectedCount = selectedCount;
    }

    public double getPlacementRate() {
        return placementRate;
    }

    public void setPlacementRate(double placementRate) {
        this.placementRate = placementRate;
    }

    public double getAveragePackage() {
        return averagePackage;
    }

    public void setAveragePackage(double averagePackage) {
        this.averagePackage = averagePackage;
    }

    public double getHighestPackage() {
        return highestPackage;
    }

    public void setHighestPackage(double highestPackage) {
        this.highestPackage = highestPackage;
    }

    public Map<String, Long> getBranchWisePlacements() {
        return branchWisePlacements;
    }

    public void setBranchWisePlacements(Map<String, Long> branchWisePlacements) {
        this.branchWisePlacements = branchWisePlacements;
    }

    public Map<String, Long> getCompanyWiseSelections() {
        return companyWiseSelections;
    }

    public void setCompanyWiseSelections(Map<String, Long> companyWiseSelections) {
        this.companyWiseSelections = companyWiseSelections;
    }

    public Map<String, Long> getStatusDistribution() {
        return statusDistribution;
    }

    public void setStatusDistribution(Map<String, Long> statusDistribution) {
        this.statusDistribution = statusDistribution;
    }
}
