package com.college.placement.dto;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResultDto {

    private boolean eligible;
    private List<String> reasons = new ArrayList<>();

    public EligibilityResultDto() {}

    public EligibilityResultDto(boolean eligible, List<String> reasons) {
        this.eligible = eligible;
        this.reasons = reasons != null ? reasons : new ArrayList<>();
    }

    public static EligibilityResultDto eligible() {
        return new EligibilityResultDto(true, new ArrayList<>());
    }

    public static EligibilityResultDto ineligible(List<String> reasons) {
        return new EligibilityResultDto(false, reasons);
    }

    public static EligibilityResultDto ineligible(String reason) {
        List<String> list = new ArrayList<>();
        list.add(reason);
        return new EligibilityResultDto(false, list);
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public String getFormattedReasons() {
        return String.join("; ", reasons);
    }
}
