package com.college.placement.dto;

import com.college.placement.entity.JobType;

public class JobFilterDto {

    private String keyword;
    private Long companyId;
    private String branch;
    private Double minPackage;
    private Double maxMinCgpa;
    private JobType jobType;
    private String location;

    public JobFilterDto() {}

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Double getMinPackage() {
        return minPackage;
    }

    public void setMinPackage(Double minPackage) {
        this.minPackage = minPackage;
    }

    public Double getMaxMinCgpa() {
        return maxMinCgpa;
    }

    public void setMaxMinCgpa(Double maxMinCgpa) {
        this.maxMinCgpa = maxMinCgpa;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
